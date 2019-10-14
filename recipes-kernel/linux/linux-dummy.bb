SUMMARY = "Dummy Linux kernel"
DESCRIPTION = "Dummy Linux kernel, to be selected as the preferred \
provider for virtual/kernel to satisfy dependencies for situations \
where you wish to build the kernel externally from the build system."
SECTION = "kernel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel siteinfo

PV = "5.3.6"
KERNEL_VERSION = "${PV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-5.3.y"
SRCREV_pn-${PN} = "a2fc8ee6676067f27d2f5c6e4d512adff3d9938c"
S = "${WORKDIR}/git"

do_configure() {
	yes '' | oe_runmake -C ${S} O=${B} allnoconfig
}

do_compile () {
	:
}

do_shared_workdir () {
	:
}

do_install() {
        oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${KERNEL_VERSION} ARCH=$ARCH

	# Stash fake data to get around the check in rootfs.py
	install -d ${D}${datadir}/kernel-depmod/
	echo "${KERNEL_VERSION}" > ${D}${datadir}/kernel-depmod/kernel-abiversion
	touch ${D}${datadir}/kernel-depmod/System.map-${KERNEL_VERSION}
}

PACKAGES_DYNAMIC += "^kernel-module-.*"
PACKAGES_DYNAMIC += "^kernel-image-.*"
PACKAGES_DYNAMIC += "^kernel-firmware-.*"

PACKAGES =+ "kernel-headers"
FILES_kernel-headers = "${exec_prefix}/src/linux*"

FILES_kernel-modules = ""
ALLOW_EMPTY_kernel-modules = "1"
DESCRIPTION_kernel-modules = "Kernel modules meta package"

do_bundle_initramfs() {
	:
}

do_deploy() {
	:
}

do_kernel_link_images() {
	:
}

# Only stage the files we need for depmod, not the modules/firmware
sysroot_stage_all () {
	sysroot_stage_dir ${D}${datadir}/kernel-depmod ${SYSROOT_DESTDIR}${datadir}/kernel-depmod
}

emit_depmod_pkgdata() {
	# Stash data for depmod
	install -d ${PKGDESTWORK}/kernel-depmod/
	echo "${KERNEL_VERSION}" > ${PKGDESTWORK}/kernel-depmod/kernel-abiversion
	touch ${PKGDESTWORK}/kernel-depmod/System.map-${KERNEL_VERSION}
}

PACKAGEFUNCS += "emit_depmod_pkgdata"
 
addtask bundle_initramfs after do_install before do_deploy
addtask deploy after do_install
addtask shared_workdir after do_compile before do_install
