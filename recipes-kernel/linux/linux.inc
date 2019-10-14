DESCRIPTION = "Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel siteinfo

KERNEL_CONFIG_COMMAND = "oe_runmake_call -C ${S} CC="${KERNEL_CC}" O=${B} olddefconfig || oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" oldnoconfig"

PATCHTOOL = "git"
PATCH_COMMIT_FUNCTIONS = "1"

ALLOW_EMPTY_kernel-devicetree = "1"

do_configure_prepend() {

        echo "" > ${B}/.config

	#
	# endian support
	#
        if [ "${SITEINFO_ENDIANNESS}" = "be" ]; then
                echo "CONFIG_CPU_BIG_ENDIAN=y"          >> ${B}/.config
        fi

        echo "CONFIG_CMDLINE=\"${CMDLINE}\"" >> ${B}/.config

        sed -e '/CONFIG_OABI_COMPAT=/d' \
            -e '/CONFIG_CMDLINE=/d' \
            -e '/CONFIG_CPU_BIG_ENDIAN/d' \
            -e '/CONFIG_LOGO=/d' \
            -e '/CONFIG_LOGO_LINUX_CLUT224=/d' \
            -e '/CONFIG_LOCALVERSION/d' \
            -e '/CONFIG_LOCALVERSION_AUTO/d' \
	    < '${WORKDIR}/defconfig' >>'${B}/.config'

        echo 'CONFIG_LOCALVERSION=""' >>${B}/.config
        echo '# CONFIG_LOCALVERSION_AUTO is not set' >>${B}/.config

        #
        # Udev quirks
        #

        # Newer versions of udev mandate that sysfs doesn't have deprecated entries
            sed -e /CONFIG_SYSFS_DEPRECATED/d \
                -e /CONFIG_SYSFS_DEPRECATED_V2/d \
                -e /CONFIG_HOTPLUG/d \
                -e /CONFIG_UEVENT_HELPER_PATH/d \
                -e /CONFIG_UNIX/d \
                -e /CONFIG_SYSFS/d \
                -e /CONFIG_PROC_FS/d \
                -e /CONFIG_TMPFS/d \
                -e /CONFIG_INOTIFY_USER/d \
                -e /CONFIG_SIGNALFD/d \
                -e /CONFIG_TMPFS_POSIX_ACL/d \
                -e /CONFIG_BLK_DEV_BSG/d \
                -e /CONFIG_AUTOFS4_FS/d \
                -i '${B}/.config'

            echo '# CONFIG_SYSFS_DEPRECATED is not set' >> ${B}/.config
            echo '# CONFIG_SYSFS_DEPRECATED_V2 is not set' >> ${B}/.config
            echo 'CONFIG_HOTPLUG=y' >> ${B}/.config
            echo 'CONFIG_UEVENT_HELPER_PATH=""' >> ${B}/.config
            echo 'CONFIG_UNIX=y' >> ${B}/.config
            echo 'CONFIG_SYSFS=y' >> ${B}/.config
            echo 'CONFIG_PROC_FS=y' >> ${B}/.config
            echo 'CONFIG_TMPFS=y' >> ${B}/.config
            echo 'CONFIG_INOTIFY_USER=y' >> ${B}/.config
            echo 'CONFIG_SIGNALFD=y' >> ${B}/.config
            echo 'CONFIG_TMPFS_POSIX_ACL=y' >> ${B}/.config
            echo 'CONFIG_BLK_DEV_BSG=y' >> ${B}/.config
            echo 'CONFIG_DEVTMPFS=y' >> ${B}/.config
            echo 'CONFIG_DEVTMPFS_MOUNT=y' >> ${B}/.config
            echo 'CONFIG_AUTOFS4_FS=y' >> ${B}/.config

        # Newer inits like systemd need cgroup support
            sed -e /CONFIG_CGROUP_SCHED/d \
                -e /CONFIG_CGROUPS/d \
                -i '${B}/.config'
            
            echo 'CONFIG_CGROUP_SCHED=y' >> ${B}/.config
            echo 'CONFIG_CGROUPS=y' >> ${B}/.config
            echo 'CONFIG_CGROUP_NS=y' >> ${B}/.config
            echo 'CONFIG_CGROUP_FREEZER=y' >> ${B}/.config
            echo 'CONFIG_CGROUP_DEVICE=y' >> ${B}/.config
            echo 'CONFIG_CPUSETS=y' >> ${B}/.config
            echo 'CONFIG_PROC_PID_CPUSET=y' >> ${B}/.config
            echo 'CONFIG_CGROUP_CPUACCT=y' >> ${B}/.config
            echo 'CONFIG_RESOURCE_COUNTERS=y' >> ${B}/.config

        #
        # root-over-nfs-over-usb-eth support. Limited, but should cover some cases.
        # Enable this by setting a proper CMDLINE_NFSROOT_USB.
        #
        if [ ! -z "${CMDLINE_NFSROOT_USB}" ]; then
                oenote "Configuring the kernel for root-over-nfs-over-usb-eth with CMDLINE ${CMDLINE_NFSROOT_USB}"
                sed -e '/CONFIG_INET/d' \
                    -e '/CONFIG_IP_PNP=/d' \
                    -e '/CONFIG_USB_GADGET=/d' \
                    -e '/CONFIG_USB_GADGET_SELECTED=/d' \
                    -e '/CONFIG_USB_ETH=/d' \
                    -e '/CONFIG_NFS_FS=/d' \
                    -e '/CONFIG_ROOT_NFS=/d' \
                    -e '/CONFIG_CMDLINE=/d' \
                    -i ${B}/.config
                echo "CONFIG_INET=y"                     >> ${B}/.config
                echo "CONFIG_IP_PNP=y"                   >> ${B}/.config
                echo "CONFIG_USB_GADGET=y"               >> ${B}/.config
                echo "CONFIG_USB_GADGET_SELECTED=y"      >> ${B}/.config
                echo "CONFIG_USB_ETH=y"                  >> ${B}/.config
                echo "CONFIG_NFS_FS=y"                   >> ${B}/.config
                echo "CONFIG_ROOT_NFS=y"                 >> ${B}/.config
                echo "CONFIG_CMDLINE=\"${CMDLINE_NFSROOT_USB}\"" >> ${B}/.config
        fi

        echo 'CONFIG_FHANDLE=y' >> ${B}/.config

        # Check for kernel config fragments.  The assumption is that the config
        # fragment will be specified with the absolute path.  For example:
        #   * ${WORKDIR}/config1.cfg
        #   * ${S}/config2.cfg
        # Iterate through the list of configs and make sure that you can find
        # each one.  If not then error out.
        # NOTE: If you want to override a configuration that is kept in the kernel
        #       with one from the OE meta data then you should make sure that the
        #       OE meta data version (i.e. ${WORKDIR}/config1.cfg) is listed
        #       after the in kernel configuration fragment.
        # Check if any config fragments are specified.
        if [ ! -z "${KERNEL_CONFIG_FRAGMENTS}" ]
        then
            for f in ${KERNEL_CONFIG_FRAGMENTS}
            do  
                # Check if the config fragment was copied into the WORKDIR from
                # the OE meta data
                if [ ! -e "$f" ]
                then
                    echo "Could not find kernel config fragment $f"
                    exit 1
                fi
            done

            # Now that all the fragments are located merge them.
            ( cd ${WORKDIR} && ${S}/scripts/kconfig/merge_config.sh -m -r -O ${B} ${B}/.config ${KERNEL_CONFIG_FRAGMENTS} 1>&2 )
        fi

	yes '' | oe_runmake -C ${S} O=${B} oldconfig
        oe_runmake -C ${S} O=${B} savedefconfig && cp ${B}/defconfig ${WORKDIR}/defconfig.saved
}

EXTRA_OEMAKE += "LOADADDR=${UBOOT_LOADADDRESS}"

do_install_append() {
        for DTB in ${KERNEL_DEVICETREE}; do
                DTB=`normalize_dtb "${DTB}"`
                DTB_EXT=${DTB##*.}
                DTB_BASE_NAME=`basename ${DTB} ."${DTB_EXT}"`
                for type in ${KERNEL_IMAGETYPE_FOR_MAKE}; do
                        symlink_name=${type}"-"${KERNEL_IMAGE_SYMLINK_NAME}
                        DTB_SYMLINK_NAME=`echo ${symlink_name} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
                        rm -f ${D}/${KERNEL_IMAGEDEST}/devicetree-${DTB_SYMLINK_NAME}.${DTB_EXT}
                done
        done
}

do_deploy_append() {
        for DTB in ${KERNEL_DEVICETREE}; do
                DTB=`normalize_dtb "${DTB}"`
                DTB_EXT=${DTB##*.}
                DTB_BASE_NAME=`basename ${DTB} ."${DTB_EXT}"`
                for type in ${KERNEL_IMAGETYPE_FOR_MAKE}; do
                        base_name=${type}"-"${KERNEL_IMAGE_BASE_NAME}
                        symlink_name=${type}"-"${KERNEL_IMAGE_SYMLINK_NAME}
                        DTB_NAME=`echo ${base_name} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
                        DTB_SYMLINK_NAME=`echo ${symlink_name} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
                        DTB_PATH=`get_real_dtb_path_in_kernel "${DTB}"`
                        rm -f ${DEPLOYDIR}/${DTB_SYMLINK_NAME}.${DTB_EXT}
                        rm -f ${DEPLOYDIR}/${DTB_BASE_NAME}.${DTB_EXT}
			rm -f ${DEPLOYDIR}/${DTB_NAME}.${DTB_EXT}

                        install -d ${DEPLOYDIR}
                        install -m 0644 ${DTB_PATH} ${DEPLOYDIR}/
                done
        done
}

do_deploy_append() {
	for dtbf in ${KERNEL_DEVICETREE}; do
		dtb=`normalize_dtb "$dtbf"`
		dtb_ext=${dtb##*.}
		dtb_base_name=`basename $dtb .$dtb_ext`

		rm -f ${DEPLOYDIR}/$dtb_base_name-${KERNEL_DTB_NAME}.$dtb_ext
		rm -f ${DEPLOYDIR}/$dtb_base_name-${KERNEL_DTB_LINK_NAME}.$dtb_ext

		install -m 0644 ${D}/${KERNEL_IMAGEDEST}/$dtb_base_name.$dtb_ext ${DEPLOYDIR} 
	done
}

# Automatically depend on lzop/lz4-native if CONFIG_KERNEL_LZO/LZ4 is enabled
python () {
    try:
        defconfig = bb.fetch2.localpath('file://defconfig', d)
    except bb.fetch2.FetchError:
        bb.warn("Fetch error for file://defconfig")
        return

    try:
        configfile = open(defconfig)
    except IOError:
        bb.warn("IO error opening file://defconfig")
        return

    # bb.warn("Defconfig OK, checking for LZ*")

    lines = str(configfile.readlines())
    # bb.warn(line)
    if (lines.find('CONFIG_KERNEL_LZO=y')  != -1):
        # bb.warn("LZO enabled in kernel, adding 'lzop-native' to DEPENDS")
        depends = d.getVar('DEPENDS', False)
        d.setVar('DEPENDS', depends + ' lzop-native')

    if (lines.find('CONFIG_KERNEL_LZ4=y')  != -1):
        # bb.warn("LZ4 enabled in kernel, adding 'lz4-native' to DEPENDS")
        depends = d.getVar('DEPENDS', False)
        d.setVar('DEPENDS', depends + ' lz4-native')

    configfile.close()	
}
