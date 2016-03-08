
from functools import partial

from diamond.collector import Collector

import psutil


def get_process_env_map(pid):
    return dict((x.split('=', 1) for x in open('/proc/%i/environ' % pid).read().split('\x00') if x and '=' in x and not x.startswith('=')))


class ProcessCollector(Collector):

    def __init__(self, *args, **kwargs):
        super(ProcessCollector, self).__init__(*args, **kwargs)
        self.processes = processes = {}
        for name, conf in self.config['process'].iteritems():
            processes[name] = pconf = {}
            instance_env_key = conf.get('instance_env_key', None)
            if instance_env_key:
                pconf['instance_env_key'] = instance_env_key

            env = conf['env']
            if isinstance(env, basestring):
                env = map(str.strip, env.split(','))

            pconf['env'] = dict((_.split(':') for _ in env))


    def get_default_config_help(self):
        config_help = super(ProcessCollector, self).get_default_config_help()
        config_help.update({
        })
        return config_help

    def get_default_config(self):
        """
        Returns the default collector settings
        """
        config = super(ProcessCollector, self).get_default_config()
        config.update({
            'path':     'processes'
        })
        return config

    def _all_env_matchers_match(self, proc_env, mproc):
        for envkey,envval in mproc['env'].iteritems():
            if not (envkey in proc_env and proc_env[envkey] == envval):
                return False
        if 'instance_env_key' in mproc and \
           not (mproc['instance_env_key'] in proc_env and proc_env[mproc['instance_env_key']]):
            return False
        return True

    def _match_and_get_name_func(self, process):
        proc_env = get_process_env_map(process.pid)
        if not proc_env:
            # the map is empty, so it can't match anything
            return False
        for name,mproc in self.processes.iteritems():
            if self._all_env_matchers_match(proc_env, mproc):
                parts = [name]
                if 'instance_env_key' in mproc:
                    parts.append(proc_env[mproc['instance_env_key']])
                name_prefix = partial(self._join, *parts)
                return name_prefix
        return False


    def collect(self):
        for process in psutil.process_iter():
            try:
                name_prefix = self._match_and_get_name_func(process)
            except Exception, e:
                self.log.exception("Error while determining if we should collect metrics for process %s" % process.pid, e)
                continue
            if not name_prefix:
                continue

            for mname,mvalue in self.collect_process_info(name_prefix, process):
                self.publish(mname, mvalue)

    def _join(self, *args):
        #TODO: memoize?
        return '.'.join(args)

    info_keys = [
        'num_ctx_switches',
        'num_threads',
        'num_fds',
        'cpu_times',
        'io_counters',
        'memory_percent',
        'memory_info',
    ]

    def collect_process_info(self, name_prefix, process):
        for key,value in process.as_dict(self.info_keys).iteritems():
            if isinstance(value, (float,int)):
                yield name_prefix(key), value
            elif isinstance(value, tuple) and hasattr(value, '_fields'):
                for k,v in zip(value._fields, value):
                    yield name_prefix(key, k), v

