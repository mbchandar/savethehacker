"""Generated message classes for developerprojects version v1.

Developer Projects API.
"""
# NOTE: This file is autogenerated and should not be edited by hand.

from protorpc import messages

from googlecloudapis.apitools.base.py import encoding


package = 'developerprojects'


class Binding(messages.Message):
  """Associates members of various types with roles. See below for details of
  the various objects that can be included as members.

  Fields:
    members: Format of member entries: 1. * Matches any requesting principal
      (users, service accounts or anonymous).  2. user:{emailid} A google user
      account using an email address. For example alice@gmail.com,
      joe@example.com  3. serviceAccount:{emailid} An service account email.
      4. group:{emailid} A google group with an email address. For example
      auth-ti-cloud@google.com  5. domain:{domain} A Google Apps domain name.
      For example google.com, example.com
    role: The name of the role to which the members should be bound. Examples:
      "roles/reader", "roles/writer", "roles/owner".
  """

  members = messages.StringField(1, repeated=True)
  role = messages.StringField(2)


class Condition(messages.Message):
  """A condition to be met.

  Enums:
    IamValueValuesEnum: Trusted attributes supplied by the IAM system.
    OpValueValuesEnum: An operator to apply the subject with.
    SysValueValuesEnum: Trusted attributes supplied by any service that owns
      resources and uses the IAM system for access control.

  Fields:
    iam: Trusted attributes supplied by the IAM system.
    op: An operator to apply the subject with.
    svc: Trusted attributes discharged by the service.
    sys: Trusted attributes supplied by any service that owns resources and
      uses the IAM system for access control.
    value: The object of the condition. Exactly one of these must be set.
    values: The objects of the condition. This is mutually exclusive with
      'value'.
  """

  class IamValueValuesEnum(messages.Enum):
    """Trusted attributes supplied by the IAM system.

    Values:
      attribution: <no description>
      authority: <no description>
      noAttr: <no description>
    """
    attribution = 0
    authority = 1
    noAttr = 2

  class OpValueValuesEnum(messages.Enum):
    """An operator to apply the subject with.

    Values:
      discharged: <no description>
      equals: <no description>
      in_: <no description>
      noOp: <no description>
      notEquals: <no description>
      notIn: <no description>
    """
    discharged = 0
    equals = 1
    in_ = 2
    noOp = 3
    notEquals = 4
    notIn = 5

  class SysValueValuesEnum(messages.Enum):
    """Trusted attributes supplied by any service that owns resources and uses
    the IAM system for access control.

    Values:
      ip: <no description>
      name: <no description>
      noAttr: <no description>
      region: <no description>
      service: <no description>
    """
    ip = 0
    name = 1
    noAttr = 2
    region = 3
    service = 4

  iam = messages.EnumField('IamValueValuesEnum', 1)
  op = messages.EnumField('OpValueValuesEnum', 2)
  svc = messages.StringField(3)
  sys = messages.EnumField('SysValueValuesEnum', 4)
  value = messages.StringField(5)
  values = messages.StringField(6, repeated=True)


class CreateProjectRequest1(messages.Message):
  """The request sent to the CreateProject method.

  Fields:
    appengineStorageLocation: The storage location for the AppEngine app.
    createAppengineProject: If true, an AppEngine project will be created.
    project: The project the user wishes to create. The user must supply the
      'project_id' field, and may optionally set the 'title' field. All other
      fields must be unset.  If the requested id is unavailable, the request
      will fail.
  """

  appengineStorageLocation = messages.StringField(1)
  createAppengineProject = messages.BooleanField(2)
  project = messages.MessageField('Project', 3)


class DeveloperprojectsProjectsCreateRequest(messages.Message):
  """A DeveloperprojectsProjectsCreateRequest object.

  Fields:
    appengineStorageLocation: The storage location for the AppEngine app.
    createAppengineProject: If true, an AppEngine project will be created.
    project: A Project resource to be passed as the request body.
  """

  appengineStorageLocation = messages.StringField(1)
  createAppengineProject = messages.BooleanField(2, default=True)
  project = messages.MessageField('Project', 3)


class DeveloperprojectsProjectsDeleteRequest(messages.Message):
  """A DeveloperprojectsProjectsDeleteRequest object.

  Fields:
    projectId: A reference that uniquely identifies the project.
  """

  projectId = messages.StringField(1, required=True)


class DeveloperprojectsProjectsDeleteResponse(messages.Message):
  """An empty DeveloperprojectsProjectsDelete response."""


class DeveloperprojectsProjectsGetRequest(messages.Message):
  """A DeveloperprojectsProjectsGetRequest object.

  Fields:
    projectId: The unique identifier of a project.
  """

  projectId = messages.StringField(1, required=True)


class DeveloperprojectsProjectsListRequest(messages.Message):
  """A DeveloperprojectsProjectsListRequest object.

  Fields:
    maxResults: Maximum number of items to return on a single page.
    pageToken: Pagination token.
    query: A query expression for filtering the results of the request.
  """

  maxResults = messages.IntegerField(1, variant=messages.Variant.INT32)
  pageToken = messages.StringField(2)
  query = messages.StringField(3)


class DeveloperprojectsProjectsUndeleteRequest(messages.Message):
  """A DeveloperprojectsProjectsUndeleteRequest object.

  Fields:
    projectId: A reference that uniquely identifies the project.
  """

  projectId = messages.StringField(1, required=True)


class DeveloperprojectsProjectsUndeleteResponse(messages.Message):
  """An empty DeveloperprojectsProjectsUndelete response."""


class DeveloperprojectsV1GetIamPolicyRequest(messages.Message):
  """A DeveloperprojectsV1GetIamPolicyRequest object.

  Fields:
    resource: REQUIRED: The resource for which policy is being requested.
      Usually some path like projects/{project}.
  """

  resource = messages.StringField(1, required=True)


class DeveloperprojectsV1SetIamPolicyRequest(messages.Message):
  """A DeveloperprojectsV1SetIamPolicyRequest object.

  Fields:
    resource: REQUIRED: The resource for which policy is being specified.
      Usually some path like projects/{project}/zones/{zone}/disks/{disk}.
    setIamPolicyRequest: A SetIamPolicyRequest resource to be passed as the
      request body.
  """

  resource = messages.StringField(1, required=True)
  setIamPolicyRequest = messages.MessageField('SetIamPolicyRequest', 2)


class ListProjectsResponse(messages.Message):
  """A page of the response received from the ListProjects method.  A
  paginated response where more pages are available will have
  `next_page_token` set. This token can be used in a subsequent request to
  retrieve the next request page.

  Fields:
    nextPageToken: Pagination token.  If the result set is too large to fit in
      a single response, this token will be filled in. It encodes the position
      of the current result cursor. Feeding this value into a new list request
      as 'page_token' parameter gives the next page of the results.  When
      next_page_token is not filled in, there is no next page and the client
      is looking at the last page in the result set.  Pagination tokens have a
      limited lifetime.
    projects: The list of projects that matched the list query, possibly
      paginated.  The resource is partially filled in, based on the
      retrieval_options specified in the `retrieval_options` field of the list
      request.
  """

  nextPageToken = messages.StringField(1)
  projects = messages.MessageField('Project', 2, repeated=True)


class LogConfig(messages.Message):
  """Specifies what kind of log the caller must write

  Fields:
    counter: Counter options.
  """

  counter = messages.MessageField('LogConfigCounterOptions', 1)


class LogConfigCounterOptions(messages.Message):
  """Options for counters

  Fields:
    field: The field value to attribute.
    metric: The metric to update.
  """

  field = messages.StringField(1)
  metric = messages.StringField(2)


class Policy(messages.Message):
  """# Overview  The `Policy` defines an access control policy language. It
  can be used to define policies that can be attached to resources like files,
  folders, VMs, etc.    # Policy structure  A `Policy` consists of a list of
  bindings. A `Binding` binds a set of members to a role, where the members
  can include user accounts, user groups, user domains, and service accounts.
  A role is a named set of permissions, defined by the IAM system. The
  definition of a role is outside the policy.  A permission check involves
  determining the roles that include the specified permission, and then
  determining if the principal specified by the check is a member of a binding
  to at least one of these roles. The membership check is recursive when a
  group is bound to a role.  Policy examples:  ``` { "bindings": [ { "role":
  "roles/owner", "members": [ "user:mike@example.com",
  "group::admins@example.com", "domain:google.com",
  "serviceAccount:frontend@example.iam.gserviceaccounts.com"] }, { "role":
  "roles/reader", "members": ["user:sean@example.com"] } ] } ```

  Fields:
    bindings: It is an error to specify multiple bindings for the same role.
      It is an error to specify a binding with no members.
    etag: Can be used to perform a read-modify-write.
    rules:
    version: The policy language version. The version of the policy itself is
      represented by the etag. The current version is 0.
  """

  bindings = messages.MessageField('Binding', 1, repeated=True)
  etag = messages.BytesField(2)
  rules = messages.MessageField('Rule', 3, repeated=True)
  version = messages.IntegerField(4, variant=messages.Variant.INT32)


class Project(messages.Message):
  """Project message type.

  Enums:
    Deprecated5ValueValuesEnum:
    LifecycleStateValueValuesEnum: The project lifecycle state.  This field is
      read-only.

  Fields:
    appengineName: Name identifying legacy projects. This field should not be
      used for new projects. This field is read-only.
    createdMs: The time at which the project was created in milliseconds since
      the epoch.  This is a read-only field.
    deprecated5: A Deprecated5ValueValuesEnum attribute.
    labels: The labels associated with this project.
    lifecycleState: The project lifecycle state.  This field is read-only.
    parent: An optional reference to a parent resource for the project. For
      example, this may point to a resource group or a billing account.  This
      is a read-write field.
    projectId: The unique, user-assigned id of the project. The id must be
      6?30 lowercase letters, digits, or hyphens. It must start with a letter.
      Trailing hyphens are prohibited.  Example: "tokyo-rain-123" This field
      is read-only once set.
    projectNumber: The number uniquely identifying the project.  Example:
      415104041262 This field is read-only.
    title: The user-assigned title of the project. This field is optional and
      may remain unset.  Example: "My Project"  This is a read-write field.
  """

  class Deprecated5ValueValuesEnum(messages.Enum):
    """Deprecated5ValueValuesEnum enum type.

    Values:
      deprecated0: <no description>
      deprecated1: <no description>
      deprecated2: <no description>
      deprecated3: <no description>
      deprecated4: <no description>
    """
    deprecated0 = 0
    deprecated1 = 1
    deprecated2 = 2
    deprecated3 = 3
    deprecated4 = 4

  class LifecycleStateValueValuesEnum(messages.Enum):
    """The project lifecycle state.  This field is read-only.

    Values:
      lifecycleActive: <no description>
      lifecycleDeleteIrreversible: <no description>
      lifecycleDeleteReversible: <no description>
      lifecycleDeleted: <no description>
      lifecycleUnknown: <no description>
    """
    lifecycleActive = 0
    lifecycleDeleteIrreversible = 1
    lifecycleDeleteReversible = 2
    lifecycleDeleted = 3
    lifecycleUnknown = 4

  appengineName = messages.StringField(1)
  createdMs = messages.IntegerField(2)
  deprecated5 = messages.EnumField('Deprecated5ValueValuesEnum', 3)
  labels = messages.MessageField('ProjectLabelsEntry', 4, repeated=True)
  lifecycleState = messages.EnumField('LifecycleStateValueValuesEnum', 5)
  parent = messages.MessageField('ResourceRef', 6)
  projectId = messages.StringField(7)
  projectNumber = messages.IntegerField(8)
  title = messages.StringField(9)


class ProjectLabelsEntry(messages.Message):
  """A ProjectLabelsEntry object.

  Fields:
    key: A string attribute.
    value: A string attribute.
  """

  key = messages.StringField(1)
  value = messages.StringField(2)


class ResourceRef(messages.Message):
  """A reference to another resource.

  Fields:
    id: Required field. The id of the object referenced.
    type: Required field. The type of object referenced.
  """

  id = messages.StringField(1)
  type = messages.StringField(2)


class Rule(messages.Message):
  """A rule to be applied in a Policy.

  Enums:
    ActionValueValuesEnum: Required

  Fields:
    action: Required
    conditions: Additional restrictions that must be met
    description: Human-readable description of the rule.
    in_: The rule matches if the PRINCIPAL/AUTHORITY_SELECTOR is in this set
      of entries.
    logConfig: The config returned to callers of tech.iam.IAM.CheckPolicy for
      any entries that match the LOG action.
    notIn: The rule matches if the PRINCIPAL/AUTHORITY_SELECTOR is not in this
      set of entries.
    permissions: A permission is a string of form '..' (e.g.,
      'storage.buckets.list'). A value of '*' matches all permissions, and a
      verb part of '*' (e.g., 'storage.buckets.*') matches all verbs.
  """

  class ActionValueValuesEnum(messages.Enum):
    """Required

    Values:
      allow: <no description>
      allowWithLog: <no description>
      deny: <no description>
      denyWithLog: <no description>
      log: <no description>
      noAction: <no description>
    """
    allow = 0
    allowWithLog = 1
    deny = 2
    denyWithLog = 3
    log = 4
    noAction = 5

  action = messages.EnumField('ActionValueValuesEnum', 1)
  conditions = messages.MessageField('Condition', 2, repeated=True)
  description = messages.StringField(3)
  in_ = messages.StringField(4, repeated=True)
  logConfig = messages.MessageField('LogConfig', 5, repeated=True)
  notIn = messages.StringField(6, repeated=True)
  permissions = messages.StringField(7, repeated=True)


class SetIamPolicyRequest(messages.Message):
  """Request message for `SetIamPolicy` method.

  Fields:
    changeDescription: Description of the change.
    policy: REQUIRED: The complete policy to be applied to the 'resource'. The
      size of the policy is limited to a few 10s of KB. An empty policy is in
      general a valid policy but certain services (like Projects) might reject
      them.
    wait: If true the SetPolicy call returns only after it is guaranteed that
      a subsequent CheckPolicy call will observe the new policy. NOTE: We may
      support this guarantee only for policy changes that broaden access.
  """

  changeDescription = messages.StringField(1)
  policy = messages.MessageField('Policy', 2)
  wait = messages.BooleanField(3)


class StandardQueryParameters(messages.Message):
  """Query parameters accepted by all methods.

  Enums:
    AltValueValuesEnum: Data format for the response.

  Fields:
    alt: Data format for the response.
    fields: Selector specifying which fields to include in a partial response.
    key: API key. Your API key identifies your project and provides you with
      API access, quota, and reports. Required unless you provide an OAuth 2.0
      token.
    oauth_token: OAuth 2.0 token for the current user.
    prettyPrint: Returns response with indentations and line breaks.
    quotaUser: Available to use for quota purposes for server-side
      applications. Can be any arbitrary string assigned to a user, but should
      not exceed 40 characters. Overrides userIp if both are provided.
    trace: A tracing token of the form "token:<tokenid>" or "email:<ldap>" to
      include in api requests.
    userIp: IP address of the site where the request originates. Use this if
      you want to enforce per-user limits.
  """

  class AltValueValuesEnum(messages.Enum):
    """Data format for the response.

    Values:
      json: Responses with Content-Type of application/json
    """
    json = 0

  alt = messages.EnumField('AltValueValuesEnum', 1, default=u'json')
  fields = messages.StringField(2)
  key = messages.StringField(3)
  oauth_token = messages.StringField(4)
  prettyPrint = messages.BooleanField(5, default=True)
  quotaUser = messages.StringField(6)
  trace = messages.StringField(7)
  userIp = messages.StringField(8)


encoding.AddCustomJsonEnumMapping(
    Condition.OpValueValuesEnum, 'in_', 'in')
encoding.AddCustomJsonFieldMapping(
    Rule, 'in_', 'in')
