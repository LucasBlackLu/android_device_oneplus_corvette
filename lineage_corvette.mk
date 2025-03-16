#
# Copyright (C) 2021-2025 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit_only.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit from corvette device
$(call inherit-product, device/oneplus/corvette/device.mk)

# Inherit some common Lineage stuff.
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)

PRODUCT_NAME := lineage_corvette
PRODUCT_DEVICE := corvette
PRODUCT_MANUFACTURER := OnePlus
PRODUCT_BRAND := OnePlus
PRODUCT_MODEL := PJX110

PRODUCT_GMS_CLIENTID_BASE := android-oneplus

PRODUCT_BUILD_PROP_OVERRIDES += \
    BuildDesc="qssi-user 16 BP2A.250605.015 1763260541838 release-keys" \
    BuildFingerprint=OnePlus/PJX110/OP5D06L1:16/UKQ1.231108.001/U.f9aed0_1164c89_1164c8b:user/release-keys \
    DeviceName=OP5D06L1 \
    DeviceProduct=PJX110 \
    SystemDevice=OP5D06L1 \
    SystemName=PJX110
