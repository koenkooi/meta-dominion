require linux.inc

DEPENDS += "openssl-native"

DESCRIPTION = "Linux kernel"
KERNEL_IMAGETYPE ?= "zImage"

COMPATIBLE_MACHINE = "(rogue|dominion-old|dominion|beast|macbook|soekris-net6501|arietta-g25|macbook|minnow|minnowboard|fri2|beaglebone|apu2c4|revo|dlink-dns320)"

FILESPATH =. "${FILE_DIRNAME}/linux-dominion-4.19:${FILE_DIRNAME}/linux-dominion-4.19/${MACHINE}:"

S = "${WORKDIR}/git"

PV = "4.19.9"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;protocol=https;branch=linux-4.19.y"
SRCREV_pn-${PN} = "be53d23e68c29900da6b6ce486b5ab8507de94b1"

SRC_URI += " \
             file://0001-bonding-sane-default-value-for-MAX_BONDS.patch \
             file://0001-block-BFQ-default-for-single-queue-devices.patch \
             file://0002-sched-allow-CAKE-to-be-set-as-default.patch \
             file://0001-https-www.wireguard.com-install-kernel-requirements.patch \
             file://am335x-bone-scale-data.bin \
             file://am335x-evm-scale-data.bin \
             file://am335x-pm-firmware.bin \
             file://am335x-pm-firmware.elf \
             file://am43x-evm-scale-data.bin \
             file://defconfig \
             file://bbr.fragment \
             file://iosched.fragment \
             file://intel.fragment \
             file://block.fragment \
             file://btrfs.fragment \
             file://debug.fragment \
             file://systemd.fragment \
             file://cifs.fragment \
             file://nfs.fragment \
             file://mlx.fragment \
             file://tweaks.fragment \
             file://unwinder.fragment \
             file://wireguard.fragment \
            "

KERNEL_CONFIG_FRAGMENTS_append = " \
                                  ${WORKDIR}/bbr.fragment \
                                  ${WORKDIR}/iosched.fragment \
                                  ${WORKDIR}/block.fragment \
                                  ${WORKDIR}/btrfs.fragment \
                                  ${WORKDIR}/debug.fragment \
                                  ${WORKDIR}/cifs.fragment \
                                  ${WORKDIR}/systemd.fragment \
                                  ${WORKDIR}/tweaks.fragment \
                                  ${WORKDIR}/unwinder.fragment \
                                  ${WORKDIR}/wireguard.fragment \
                                 "

KERNEL_CONFIG_FRAGMENTS_append_x86 = " \
                                  ${WORKDIR}/intel.fragment \
                                  ${WORKDIR}/nfs.fragment \
                                 "

KERNEL_CONFIG_FRAGMENTS_append_x86-64 = " \
                                  ${WORKDIR}/intel.fragment \
                                  ${WORKDIR}/nfs.fragment \
                                  ${WORKDIR}/mlx.fragment \
                                 "

do_configure_prepend() {
	if [ -e ${WORKDIR}/am335x-pm-firmware.elf ] ; then
		cp ${WORKDIR}/am* ${S}/firmware/
	fi
}
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
