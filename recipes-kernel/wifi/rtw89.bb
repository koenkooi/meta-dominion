SUMMARY = "RTW89 Wi-Fi driver"
DESCRIPTION = "RealTek RTW89 driver"

LICENSE = "CLOSED"

inherit module

SRCREV = "8a8ba9bdee85c54b55efa27bd92e6475a77da7af"
SRC_URI = "git://github.com/morrownr/rtw89.git;branch=main;protocol=https \
           file://0001-Makefile-remove-PCIe-variants-they-don-t-build.patch \
           "


EXTRA_OEMAKE:append = " KDIR=${STAGING_KERNEL_DIR} KVER=${KERNEL_VERSION}"

RPROVIDES:${PN} += "kernel-module-rtw89"

do_install() {
    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra/rtw89
    install -m 0644 *.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra/rtw89

    install -d ${D}${sysconfdir}/modprobe.d
    install -m 0644 rtw89.conf ${D}${sysconfdir}/modprobe.d
}

FILES:${PN} += "${sysconfdir}"

# For dongles that come up as USB storage and need to be eject
RRECOMMENDS:${PN} += "usb-modeswitch"
