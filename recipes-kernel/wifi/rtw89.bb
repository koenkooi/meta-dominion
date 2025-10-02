SUMMARY = "RTW89 Wi-Fi driver"
DESCRIPTION = "RealTek RTW89 driver"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://rtw8851b.c;beginline=1;endline=2;md5=f61578bcdec059a3c999594e58bfc0ae"

inherit module

SRCREV = "8bdbb48aa88e83eb7dfac5e9a4ad537c707d6c4f"
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

# For dongles that come up as USB storage and need to be ejected
RRECOMMENDS:${PN} += "usb-modeswitch"
