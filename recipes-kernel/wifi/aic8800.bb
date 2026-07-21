SUMMARY = "Aicsemi aic8800 Wi-Fi driver"
DESCRIPTION = "Aicsemi aic8800 Wi-Fi driver installation"

LICENSE = "CLOSED"

inherit module

# shenmintao/aic8800d80 main (SDK v5.0). Newer than the previous snapshot and
# already carries the upstream cfg80211/newer-kernel fixes, so the old 6.19
# fallthrough backport (32.patch) is dropped as obsolete.
PV = "5.0"
SRCREV = "388a0192c707a40a684f8ce8085244b9efb8f8d6"
SRC_URI = "git://github.com/shenmintao/aic8800d80.git;branch=main;protocol=https \
           file://0001-aic8800-AICWFDBG-silent-default.patch;striplevel=3 \
          "

S = "${UNPACKDIR}/${PN}-${PV}/drivers/aic8800"

EXTRA_OEMAKE:append = " KDIR=${STAGING_KERNEL_DIR} KVER=${KERNEL_VERSION}"

# AICWFDBG logging compiles silent by default. Set AIC8800_DEBUG = "1" for a
# verbose build; the aicwf_dbg_level module parameter also raises it at runtime.
AIC8800_DEBUG ??= "0"
EXTRA_OEMAKE:append = "${@bb.utils.contains('AIC8800_DEBUG', '1', ' KCFLAGS=-DAICWF_DBG_LEVEL_DEFAULT=0xffff', '', d)}"

do_compile:prepend() {
        for mk in $(find . -name "Makefile") ; do sed -i -e 's:$(shell nproc):4:g' $mk ; done
}

do_install() {
        install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/aic8800
        install -m 0644 */*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/aic8800

        install -d ${D}${nonarch_base_libdir}/firmware
        cp -r ../../fw/* ${D}${nonarch_base_libdir}/firmware

        install -d ${D}${nonarch_base_libdir}/udev/rules.d
        install -m 0644 ../../aic.rules ${D}${nonarch_base_libdir}/udev/rules.d/40-aic.rules
}

KERNEL_MODULE_PROVIDE_VIRTUAL = "1"
KERNEL_SPLIT_MODULES = "1"
FILES:${PN} += "${nonarch_base_libdir}/firmware ${nonarch_base_libdir}/udev"
