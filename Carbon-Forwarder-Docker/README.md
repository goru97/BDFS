##How to Run
docker run -d --net host carbon-forwarder

| Environment Variables | Description | default |
| ----- | ------- | --------- |
| ENDPOINT | Endpoint to listen on for pickle protocol metrics | tcp:2004 |
| METRICS_SEND_INTERVAL | Metrics send interval, sec | 30.0 |
| BLUEFLOOD_URL | Blueflood address | http://localhost:19000 |
| TENANT_ID | Tenant ID | tenant |
| METRIC_PREFIX | Prefix to be prepended to metrics name | metric_prefix |
| DEFAULT_TTL | TimeToLive value for metrics, sec | 86400 |
| KEYSTONE_USER | Keystone user | |
| KEYSTONE_KEY | Keystone key | |
| AUTH_URL | Keystone token URL | https://identity.api.rackspacecloud.com/v2.0/tokens |
