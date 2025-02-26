package com.pingidentity.sample.configuration;

public class SpSampleConstants
{
    public static final String APP_PATH = "/app";
    public static final String AGENTLESS_SP_SAMPLE_PATH = "AgentlessSPSample";
    public static final String IDP_SAMPLE_APP_CONTEXT = "/AgentlessIdPSample" + APP_PATH;

    public static final String ERROR_JSP_PATH = "/error.jsp";
    public static final String NOT_FOUND_JSP_PATH = "/notFound.jsp";

    public static final String APP_JSP_PATH = "/WEB-INF/jsp/app.jsp";
    public static final String LOGOUT_JSP_PATH = "/WEB-INF/jsp/logout.jsp";
    public static final String LOGGED_OUT_JSP_PATH = "/WEB-INF/jsp/logged_out.jsp";
    public static final String CONFIGURATION_JSP_PATH = "/WEB-INF/jsp/configuration.jsp";

    public static final String ACTION_PARAM = "action";
    public static final String ACTION_PARAM_CONFIGURE = "configure";
    public static final String ACTION_PARAM_LOGOUT = "logout";
    public static final String ACTION_PARAM_LOGGED_OUT = "logged_out";

    public static final String APP_KEY_PREFIX = "APP_KEY_PREFIX";
    public static final String LOGOUT_KEY_PREFIX = "LOGOUT_KEY_PREFIX";

    public static final String ERROR = "error";

    public static final String SP_ADAPTER_CONF_BASE_PF_URL = "basePfUrl";
    public static final String SP_ADAPTER_CONF_USERNAME = "username";
    public static final String SP_ADAPTER_CONF_PASSPHRASE = "passphrase";
    public static final String SP_ADAPTER_CONF_USE_BEARER_TOKEN_AUTH = "useBearerTokenAuth";
    public static final String SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_ID = "ccClientId";
    public static final String SP_ADAPTER_CONF_CLIENT_CREDENTIALS_CLIENT_SECRET = "ccClientSecret";
    public static final String SP_ADAPTER_CONF_ADAPTER_ID = "adapterId";
    public static final String SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT = "outgoingAttributeFormat";
    public static final String SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_JSON = "JSON";
    public static final String SP_ADAPTER_CONF_OUTGOING_ATTRIBUTE_FORMAT_PROPERTIES = "PROPERTIES";
    public static final String SP_ADAPTER_CONF_TARGET_URL = "targetURL";
    public static final String SP_ADAPTER_CONF_PARTNER_ENTITY_ID = "partnerEntityId";

    public static final String REF_ID_ADAPTER_PICKUP_ENDPOINT = "/ext/ref/pickup";
    public static final String REQUEST_AUTHORIZATION_HEADER = "REQUEST_AUTHORIZATION_HEADER";

    public static final String REF_ID_ADAPTER_RESPONSE = "REF_ID_ADAPTER_RESPONSE";
    public static final String REF_ID_ADAPTER_REDIRECT_URL = "REF_ID_ADAPTER_REDIRECT_URL";
    public static final String REF_ID_ADAPTER_REDIRECT_URL_PATH = "REF_ID_ADAPTER_REDIRECT_URL_PATH";
    public static final String REF_ID_ADAPTER_ATTRIBUTES = "REF_ID_ADAPTER_ATTRIBUTES";

    public static final String REF_ID_ADAPTER_REFERENCE = "REF";

    public static final String REF_ID_ADAPTER_INSTANCE_ID = "ping.instanceId";
    public static final String REF_ID_ADAPTER_RESUME_PATH = "resumePath";
    public static final String TARGET_RESOURCE = "TargetResource";
    public static final String PARTNER_IDP_ID = "PartnerIdpId";

    public static final String START_SP_SSO_ENDPOINT = "/sp/startSSO.ping";
    public static final String START_SP_SLO_ENDPOINT = "/sp/startSLO.ping";
}
