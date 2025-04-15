SUMMARY = "Out of tree build for the new cake qdisc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://sch_cake.c;beginline=1;endline=36;md5=93db6d2d93d0ddf825519d65f10f5531"

# Made up version
PV = "2017.12"

SRCREV = "5bf0b6596721e18269ee4bae6e3549c75cba923a"
SRC_URI = "git://github.com/dtaht/sch_cake.git;protocol=https;branch=cobalt" 

S = "${WORKDIR}/git"

inherit module

EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"

do_install() {
	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/sched/
	install -m 0644 sch_cake.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/sched/
}

RDEPENDS_${PN} += "iproute2-tc"
