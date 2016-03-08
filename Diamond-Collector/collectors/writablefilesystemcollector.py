# coding=utf-8

import re

import diamond.collector

try:
    import psutil
    psutil  # workaround for pyflakes issue #13
except ImportError:
    psutil = None



class WritableFilesystemCollector(diamond.collector.Collector):

    def __init__(self, config, handlers):
        super(WritableFilesystemCollector, self).__init__(config, handlers)

        # Precompile things
        self.exclude_filters = self.config['exclude_filters']
        if isinstance(self.exclude_filters, basestring):
            self.exclude_filters = [self.exclude_filters]

        self.exclude_reg = re.compile('|'.join(self.exclude_filters))

        self.filesystems = []
        if isinstance(self.config['filesystems'], basestring):
            for filesystem in self.config['filesystems'].split(','):
                self.filesystems.append(filesystem.strip())
        elif isinstance(self.config['filesystems'], list):
            self.filesystems = self.config['filesystems']

    def get_default_config_help(self):
        config_help = super(WritableFilesystemCollector, self).get_default_config_help()
        config_help.update({
        })
        return config_help

    def get_default_config(self):
        """
        Returns the default collector settings
        """
        config = super(WritableFilesystemCollector, self).get_default_config()
        config.update({
            'path': 'writable-filesystem',
            # filesystems to examine
            'filesystems': 'ext2, ext3, ext4, xfs, glusterfs, nfs, ntfs, hfs,'
            + ' fat32, fat16, btrfs',

            # exclude_filters
            #   A list of regex patterns
            #   A filesystem matching any of these patterns will be excluded
            #   from disk space metrics collection.
            #
            # Examples:
            #       exclude_filters =,
            # no exclude filters at all
            #       exclude_filters = ^/boot, ^/mnt
            # exclude everything that begins /boot or /mnt
            #       exclude_filters = m,
            # exclude everything that includes the letter "m"
            'exclude_filters': ['^/export/home'],
        })
        return config

    def collect(self):
        partitions = psutil.disk_partitions(False)
        for partition in partitions:
            fs_type = partition.fstype

            # Skip the filesystem if it is not in the list of valid
            # filesystems
            if partition.fstype not in self.filesystems:
                self.log.debug("Ignoring %s since it is of type %s which "
                               + " is not in the list of filesystems.",
                               partition.mountpoint, partition.fstype)
                continue

            # Process the filters
            if self.exclude_reg.search(partition.mountpoint):
                self.log.debug("Ignoring %s since it is in the "
                               + "exclude_filter list.", partition.mountpoint)
                continue

            if (partition.mountpoint.startswith('/dev')
                or partition.mountpoint.startswith('/proc')
                    or partition.mountpoint.startswith('/sys')):
                continue

            name = partition.mountpoint.replace('/', '_')
            name = name.replace('.', '_').replace('\\', '')
            if name == '_':
                name = 'root'

            name += ".writable"

            opts = partition.opts.split(',')
            # metric value is 1 if the filesystem is writable, 0 if it is NOT writable
            metric_value = int('ro' not in opts)

            # Publish Metric
            self.publish(name, metric_value)

