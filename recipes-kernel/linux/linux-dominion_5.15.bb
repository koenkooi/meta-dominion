require linux.inc

DEPENDS += "openssl-native"

DESCRIPTION = "Linux kernel"
KERNEL_IMAGETYPE ?= "zImage"

COMPATIBLE_MACHINE = "(rogue|dominion-old|dominion|beast|macbook|soekris-net6501|arietta-g25|macbook|minnow|minnowboard|fri2|beaglebone|apu2c4|revo|dlink-dns320)"

FILESPATH =. "${FILE_DIRNAME}/linux-dominion-5.15:${FILE_DIRNAME}/linux-dominion-5.15/${MACHINE}:"

S = "${WORKDIR}/git"

PV = "5.15.12"

SRC_URI = "git://kernel.googlesource.com/pub/scm/linux/kernel/git/stable/linux.git;protocol=https;branch=linux-5.15.y"
SRCREV_pn-${PN} = "25960cafa06e6fcd830e6c792e6a7de68c1e25ed"

SRC_URI += " \
             file://0001-bonding-sane-default-value-for-MAX_BONDS.patch \
             file://0002-sched-allow-CAKE-to-be-set-as-default.patch \
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

EXTRA_OEMAKE_append = " LD=ld.bfd"
export LD="ld.bfd"
do_configure_prepend() {
	mkdir -p ${S}/firmware
	if [ -e ${WORKDIR}/am335x-pm-firmware.elf ] ; then
		cp ${WORKDIR}/am* ${S}/firmware/
	fi
}
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
