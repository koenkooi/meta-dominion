require linux.inc

DEPENDS += "openssl-native"

DESCRIPTION = "Linux kernel"
KERNEL_IMAGETYPE ?= "zImage"

COMPATIBLE_MACHINE = "(dominion-old|dominion|beast|macbook|soekris-net6501|arietta-g25|macbook|minnow|minnowboard|fri2)"

FILESPATH =. "${FILE_DIRNAME}/linux-dominion-4.6:${FILE_DIRNAME}/linux-dominion-4.6/${MACHINE}:"

S = "${WORKDIR}/git"

PV = "4.6"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git"
SRCREV_pn-${PN} = "2dcd0af568b0cf583645c8a317dd12e344b1c72a"

SRC_URI += " \
             file://0001-wireless-populate-db.txt.patch \
             file://0002-btusb-add-USB-ID-for-ASUS-X79-DELUXE-onboard-BT.patch \
             file://0003-Acme-boards-patches.patch \
             file://0004-ARM-DTS-arietta-G25-ttyS2-SPI0-ADC-on-expansion-head.patch \
             file://0005-ARM-DTS-acme-arietta-use-button-as-power-button.patch \
             file://0006-ARM-DTS-acme-arietta-drop-chosen-node.patch \
             file://defconfig \
           "
