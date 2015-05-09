# Copyright 2015 Google Inc. All Rights Reserved.

"""Cloud SDK markdown document text renderer."""

import os
import sys

from googlecloudsdk.core.document_renderers import renderer


class TextRenderer(renderer.Renderer):
  """Renders markdown to text.

  Attributes:
    _BULLET_DEDENT: Nested bullet indentation adjustment in characters.
    _INDENT: Indentation increment in characters for each level.
    _blank: True if the output already contains a blank line. Used to avoid
      sequences of 2 or more blank lines in the output.
    _bullet: List of bullet characters indexed by list level modulo #bullets.
    _csi: The ANSI Control Sequence Indicator string, used for font
      embellishments and special characters.
    _fill: The number of characters in the current output line.
    _font_tag: List of font embellishment tag string indexed by font attribute.
      Used to to form control sequences: _csi + _font_tag[n] + 'm' for font n.
    _ignore_width: True if the next output word should ignore _width.
    _indent: List of left indentations in characters indexed by _level.
    _level: The section or list level counting from 0.
    _table: True if currently rendering a table.
  """
  _BULLET_DEDENT = 2
  _INDENT = 4

  def __init__(self, *args, **kwargs):
    super(TextRenderer, self).__init__(*args, **kwargs)
    self._blank = True
    self._bullet = ('o', '*', '+', '-')
    self._csi = ''
    self._fill = 0
    self._ignore_width = False
    self._indent = [self._INDENT]
    self._level = 0
    self._table = False
    # TODO(user): TERM, sys.stdout.encoding tests should be in console_io.
    term = os.getenv('TERM', '')
    self._font_tag = (
        '1',
        '5' if 'screen' in term else '4' if 'xterm-256' in term else '3',
        '')
    if hasattr(sys.stdout, 'encoding') and sys.stdout.encoding:
      self._csi = '\033[' if 'screen' in term or 'xterm' in term else ''
      if 'UTF-8' in sys.stdout.encoding:
        self._bullet = (u'\u25CF', u'\u25A0', u'\u25C6',
                        u'\u25CB', u'\u25A1', u'\u25C7')

  def _Flush(self):
    """Flushes the current collection of Fill() lines."""
    self._ignore_width = False
    if self._fill:
      self._out.write('\n')
      self._blank = False
      self._fill = 0

  def _SetIndentation(self, level, bullet=False):
    """Sets the indentation level and character offset.

    Args:
      level: The desired indentaton level.
      bullet: True if indentation is for a bullet list.
    """
    if self._level < level:
      # Level increases are strictly 1 at a time.
      if level >= len(self._indent):
        self._indent.append(0)
      indent = self._INDENT
      if bullet and level > 1:
        # Nested bullet indentation is less than normal indent for aesthetics.
        indent -= self._BULLET_DEDENT
      self._indent[level] = self._indent[level - 1] + indent
    self._level = level

  def Example(self, line):
    """Displays line as an indented example.

    Args:
      line: The example line text.
    """
    self._fill = self._indent[self._level] + self._INDENT
    self._out.write(' ' * self._fill + line + '\n')
    self._blank = False
    self._fill = 0

  def Fill(self, line):
    """Adds a line to the output, splitting to stay within the output width.

    This is close to textwrap.wrap() except that control sequence characters
    don't count in the width computation.

    Args:
      line: The text line.
    """
    self._blank = True
    for word in line.split():
      if not self._fill:
        self._fill = self._indent[self._level] - 1
        self._out.write(' ' * self._fill)
      n = len(word)
      if self._csi:
        # Control sequence characters don't count in the width.
        i = 0
        m = n
        while i < m:
          i = word.find(self._csi, i)
          if i < 0:
            break
          j = i
          i += len(self._csi)
          while i < m and not word[i].isalpha():
            i += 1
          i += 1
          n -= i - j
      if self._fill + n + 1 >= self._width and not self._ignore_width:
        self._out.write('\n')
        self._fill = self._indent[self._level]
        self._out.write(' ' * self._fill)
      else:
        self._ignore_width = False
        if self._fill:
          self._fill += 1
          self._out.write(' ')
      self._fill += n
      self._out.write(word)

  def Finish(self):
    """Finishes all output document rendering."""
    self._Flush()
    self.Font(out=self._out)

  def Font(self, attr=None, out=None):
    """Returns the font embellishment string for attr.

    Args:
      attr: None to reset to the default font, otherwise one of renderer.BOLD,
        renderer.ITALIC, or renderer.CODE.
      out: Writes tags to this stream if not None.

    Returns:
      The font embellishment string.
    """
    if not self._csi:
      return ''
    tags = [self._csi]
    if attr is None:
      self._font = 0
    else:
      mask = 1 << attr
      self._font ^= mask
      sep = ''
      for i in range(len(self._font_tag)):
        if (self._font & (1 << i)) and self._font_tag[i]:
          tags.append(sep + self._font_tag[i])
          sep = ';'
    tags.append('m')
    embellishment = ''.join(tags)
    if out:
      out.write(embellishment)
    return embellishment

  def Heading(self, level, heading):
    """Renders a heading.

    Args:
      level: The heading level counting from 1.
      heading: The heading text.
    """
    if level == 1 and heading.endswith('(1)'):
      # Ignore man page TH.
      return
    self._Flush()
    self.Font(out=self._out)
    self._out.write(self.Font(renderer.BOLD) + heading +
                    self.Font(renderer.BOLD) + '\n\n')
    self._blank = True
    self._level = 0
    self._rows = []

  def Line(self):
    """Renders a paragraph separating line."""
    self._Flush()
    if not self._blank:
      self._blank = True
      self._out.write('\n')

  def List(self, level, definition=None):
    """Renders a bullet or definition list item.

    Args:
      level: The list nesting level, 0 if not currently in a list.
      definition: Definition list text if not None, bullet list otherwise.
    """
    self._Flush()
    if not level:
      self._level = level
    elif definition:
      self._SetIndentation(level)
      self._out.write(' ' * (self._indent[level] - self._INDENT + 1) +
                      definition + '\n')
    else:
      self._SetIndentation(level, bullet=True)
      self._out.write(' ' * (self._indent[level] - self._BULLET_DEDENT) +
                      self._bullet[(level - 1) % len(self._bullet)])
      self._fill = self._indent[level] + 1
      self._ignore_width = True

  def Synopsis(self, line):
    """Renders NAME and SYNOPSIS lines as a hanging indent.

    Collapses adjacent spaces to one space, deletes trailing space, and doesn't
    split top-level nested [...] groups. Also detects and does not count
    terminal control sequences.

    Args:
      line: The NAME or SYNOPSIS text.
    """
    nest = 0  # [...] nesting level.
    no_split = 0  # buf[no_split:i] should not be split across lines.
    # String append on buf used below because of no_split lookbehind.
    buf = ' ' * self._indent[0]
    n = len(buf) + 1
    i = 0
    while i < len(line):
      if self._csi and line[i:].startswith(self._csi):
        # Don't count terminal control sequences in the output width.
        buf += self._csi
        i += len(self._csi)
        while i < len(line):
          c = line[i]
          buf += c
          i += 1
          if c.isalpha():
            break
        continue
      c = line[i]
      if c == '[':
        # [...] nesting.
        nest += 1
        if nest == 1:
          # A new [...] group - don't split until the end of the group.
          no_split = len(buf)
      elif c in [']', ' ']:
        if c == ']':
          nest -= 1
        if not nest:
          # Outside [...]. OK to split at this point if needed.
          if n >= self._width:
            # Split the line up to no_split, eliminate trailing space and write
            # the line up to no_split.
            n = no_split
            while n > 0 and buf[n - 1] == ' ':
              n -= 1
            self._out.write(buf[:n] + '\n')
            # Reset indentation for the next line which will start at no_split.
            buf = ' ' * self._indent[0] * 2 + buf[no_split:]
            n = len(buf) + 1
          elif c == ' ':
            # Space outside [...]. Set a new split point.
            no_split = len(buf)
        if c == ' ' and buf and buf[-1] == ' ':
          # Collapse adjacent spaces to one space.
          i += 1
          continue
      buf += c
      n += 1
      i += 1
    self._out.write(buf + '\n\n')

  def Table(self, line):
    """Renders a table line.

    Nested tables are not supported. The first call on a new table is:
      Table(attributes)
    the intermediate calls add the heading and data lines and the last call is:
      Table(None)

    Args:
      line: A CSV table data line.
    """
    if line is None:
      # TODO(user): Use resource_printer.TablePrinter() when it lands.
      if self._rows:
        cols = len(self._rows[0])
        width = [0 for _ in range(cols)]
        for row in self._rows:
          for i in range(cols - 1):
            w = len(row[i])
            if width[i] <= w:
              width[i] = w + 1
        for row in self._rows:
          self._out.write(' ' * (self._indent[self._level] + 2))
          for i in range(cols - 1):
            self._out.write(row[i].ljust(width[i]))
          self._out.write(row[-1] + '\n')
        self._rows = []
      self._table = False
      self._out.write('\n')
    elif not self._table:
      self._table = True
      self.Line()
    else:
      self._rows.append(line.split(','))
