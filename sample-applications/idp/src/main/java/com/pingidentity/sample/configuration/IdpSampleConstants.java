package com.pingidentity.sample.configuration;

public class IdpSampleConstants
{
    public static final String APP_PATH = "/app";
    public static final String AGENTLESS_IDP_SAMPLE_PATH = "AgentlessIdPSample";

    public static final String ERROR_JSP_PATH = "/error.jsp";
    public static final String NOT_FOUND_JSP_PATH = "/notFound.jsp";

    public static final String LOGIN_JSP_PATH = "/WEB-INF/jsp/login.jsp";
    public static final String CONFIGURATION_JSP_PATH = "/WEB-INF/jsp/configuration.jsp";
    public static final String DROPOFF_JSP_PATH = "/WEB-INF/jsp/dropoff.jsp";
    public static final String LOGOUT_JSP_PATH = "/WEB-INF/jsp/logout.jsp";

    public static final String ERROR = "error";

    public static final String ACTION_PARAM = "action";
    public static final String ACTION_PARAM_LOGIN = "login";
    public static final String ACTION_PARAM_CONFIGURE = "configure";
    public static final String ACTION_PARAM_DROPOFF = "dropoff";
    public static final String ACTION_PARAM_RESUME = "resume";
    public static final String ACTION_PARAM_LOGOUT = "logout";

    public static final String LOGIN_KEY_PREFIX = "LOGIN_KEY_PREFIX";
    public static final String DROPOFF_KEY_PREFIX = "DROPOFF_KEY_PREFIX";
    public static final String RESUME_KEY_PREFIX = "RESUME_KEY_PREFIX";
    public static final String LOGOUT_KEY_PREFIX = "LOGOUT_KEY_PREFIX";
    public static final String REQUEST_AUTHORIZATION_HEADER = "REQUEST_AUTHORIZATION_HEADER";

    public static final String IDP_ADAPTER_CONF_BASE_PF_URL = "basePfUrl";
    public static final String IDP_ADAPTER_CONF_USERNAME = "username";
    public static final String IDP_ADAPTER_CONF_PASSPHRASE = "passphrase";
    public static final String IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID = "ccClientId";
    public static final String IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET = "ccClientSecret";
    public static final String IDP_ADAPTER_CONF_CLIENT_CREDENTIALS_FLOW_SCOPE = "ccScope";
    public static final String IDP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH = "useBearerTokenAuth";
    public static final String IDP_ADAPTER_CONF_ADAPTER_ID = "adapterId";
    public static final String IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT = "outgoingAttributeFormat";
    public static final String IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON = "JSON";
    public static final String IDP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_PROPERTIES = "PROPERTIES";
    public static final String IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT = "incomingAttributeFormat";
    public static final String IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_JSON = "JSON";
    public static final String IDP_ADAPTER_CONF_INCOMING_ATTRIBUTE_FORMAT_QUERY = "QUERY_PARAMETER";
    public static final String IDP_ADAPTER_CONF_TARGET_URL = "targetURL";
    public static final String IDP_ADAPTER_CONF_PARTNER_ENTITY_ID = "partnerEntityId";

    public static final String REF_ID_ADAPTER_PICKUP_ENDPOINT = "/ext/ref/pickup";
    public static final String REF_ID_ADAPTER_DROPOFF_ENDPOINT = "/ext/ref/dropoff";

    public static final String REF_ID_ADAPTER_REQUEST_ATTRIBUTES = "REF_ID_ADAPTER_REQUEST_ATTRIBUTES";
    public static final String REF_ID_ADAPTER_RESPONSE = "REF_ID_ADAPTER_RESPONSE";
    public static final String REF_ID_ADAPTER_REDIRECT_URL_PATH = "REF_ID_ADAPTER_REDIRECT_URL_PATH";
    public static final String REF_ID_ADAPTER_REDIRECT_URL = "REF_ID_ADAPTER_REDIRECT_URL";
    public static final String REF_ID_ADAPTER_ATTRIBUTES = "REF_ID_ADAPTER_ATTRIBUTES";

    public static final String REF_ID_ADAPTER_REFERENCE = "REF";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String REF_ID_ADAPTER_INSTANCE_ID = "ping.instanceId";
    public static final String RESUME_PATH = "resumePath";
    public static final String SUBJECT = "subject";
    public static final String AUTHN_INSTANT = "authnInst";
    public static final String USER = "user";
    public static final String TARGET_RESOURCE = "TargetResource";
    public static final String PARTNER_SP_ID = "PartnerSpId";

    public static final String START_IDP_SSO_ENDPOINT = "/idp/startSSO.ping";
}
