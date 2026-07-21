SUMMARY = "RTL8922AU (rtw89, WiFi 7 USB) Wi-Fi driver"
DESCRIPTION = "RealTek RTL8922AU driver. Every other rtw89 chip (PCIe and \
USB) is upstream in the kernel already; 8922AU is the one variant still \
missing from mainline, so this recipe builds only that one out of tree."

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://rtw8922a.c;beginline=1;endline=2;md5=4fc8191b0103f837d0786d19611fd92b"

inherit module

# morrownr/rtw89 main HEAD as of 2026-07-14; the previous pin (8bdbb48a,
# 2025-09) was 379 commits behind, including several 8922AU-specific fixes
# (RX aggregation, USB3 mode switching, MAC init/recovery fixes, new USB IDs).
SRCREV = "08b8d326937a200a706ec9c501374eec15835b5a"
SRC_URI = "git://github.com/morrownr/rtw89.git;branch=main;protocol=https \
           file://0001-Makefile-build-only-rtw8922au.patch \
           file://rtw8922au.conf \
           "


EXTRA_OEMAKE:append = " KDIR=${STAGING_KERNEL_DIR} KVER=${KERNEL_VERSION}"

RPROVIDES:${PN} += "kernel-module-rtw8922au"

do_install() {
    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra/rtw8922au
    install -m 0644 *.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra/rtw8922au

    install -d ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/rtw8922au.conf ${D}${sysconfdir}/modprobe.d
}

FILES:${PN} += "${sysconfdir}"

# For dongles that come up as USB storage and need to be ejected
RRECOMMENDS:${PN} += "usb-modeswitch"
