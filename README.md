Meta-dominion
================================

Introduction
-------------------------

The official OpenEmbedded/Yocto BSP layer for my home machines

The meta-dominion layer depends on:

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: daisy

	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: daisy

And optionally:

	URI: https://github.com/linux4sam/meta-atmel.git
	layers: .
	branch: master
	revision: ad39f6523a214fa468c970b91e2d71951e753f05


Contributing
-------------------------

Please use github for pull requests: https://github.com/koenkooi/meta-dominion/pulls

Reporting bugs
-------------------------

The github issue tracker (https://github.com/koenkooi/meta-dominion/issues) is being used to keep track of bugs.

Maintainers: Koen Kooi <koen@dominion.thruhere.net>
