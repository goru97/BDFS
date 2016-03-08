# coding=utf-8

import diamond.collector

import requests


class UnreachableCassNodesCollector(diamond.collector.Collector):

    def get_default_config_help(self):
        config_help = super(UnreachableCassNodesCollector, self).get_default_config_help()
        config_help.update({
        })
        return config_help

    def get_default_config(self):
        """
        Returns the default collector settings
        """
        config = super(UnreachableCassNodesCollector, self).get_default_config()
        config.update({
            'path': 'unreachable-cass-nodes',
            'host': 'localhost',
            'port': 7777,
        })
        return config

    def collect(self):
        url = 'http://%s:%s/jolokia/read/org.apache.cassandra.db:type=StorageService/UnreachableNodes' % (self.config['host'], self.config['port'])
        try:
            r = requests.get(url, timeout=5)
            if r.status_code == 200:
                metric_value = len(r.json()['value'])
            else:
                self.log.warn("jolokia didn't return 200")
                metric_value = -1
        except requests.exceptions.RequestException, e:
            self.log.exception("Unable to query cassandra")
            metric_value = -1

        # Publish Metric
        # the resulting metric name should be something like
        # "unreachable-cass-nodes.count"
        self.publish("count", metric_value)

