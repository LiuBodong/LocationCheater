package org.codebase.locationcheater.hook.hookers;

import android.annotation.SuppressLint;
import android.os.Build;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Pair;

import org.codebase.locationcheater.hook.ProfileDtoHolder;
import org.codebase.locationcheater.ui.dao.TelephoneDto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.annotations.AfterInvocation;
import io.github.libxposed.api.annotations.BeforeInvocation;
import io.github.libxposed.api.annotations.XposedHooker;

public class TelephoneHookers extends HookersHelper {

    public TelephoneHookers(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public List<Pair<MethodDescriptor, Class<? extends XposedInterface.Hooker>>> getHookList() {
        final String className = TelephonyManager.class.getName();
        return List.of(
                Pair.create(MethodDescriptor.of(className, "getCellLocation"), GetCellLocationHooker.class),
                Pair.create(MethodDescriptor.of(className, "getAllCellInfo"), GetAllCellInfoHooker.class));
    }

    @XposedHooker
    public static final class GetCellLocationHooker implements XposedInterface.Hooker {
        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                List<TelephoneDto> telephones = profileDto.getTelephones();
                if (!telephones.isEmpty()) {
                    TelephoneDto telephoneDto = telephones.get(0);
                    switch (telephoneDto.getType().toUpperCase()) {
                        case "CDMA":
                            CdmaCellLocation cdmaCellLocation = new CdmaCellLocation();
                            cdmaCellLocation.setCellLocationData(telephoneDto.getLac(), -1, -1, -1, telephoneDto.getCid());
                            afterHookCallback.setResult(cdmaCellLocation);
                            break;
                        case "GSM":
                            GsmCellLocation gsmCellLocation = new GsmCellLocation();
                            gsmCellLocation.setLacAndCid(telephoneDto.getLac(), telephoneDto.getCid());
                            afterHookCallback.setResult(gsmCellLocation);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @XposedHooker
    public static final class GetAllCellInfoHooker implements XposedInterface.Hooker {
        @BeforeInvocation
        public static void before(XposedInterface.BeforeHookCallback beforeHookCallback) {

        }

        @AfterInvocation
        public static void after(XposedInterface.AfterHookCallback afterHookCallback) {
            ProfileDtoHolder.doIfNonNull(profileDto -> {
                List<TelephoneDto> telephones = profileDto.getTelephones();
                if (!telephones.isEmpty()) {
                    List<CellInfo> allCellInfo = telephones.stream()
                            .map(telephoneDto -> {
                                try {
                                    switch (telephoneDto.getType().toUpperCase()) {
                                        case "CDMA":
                                            Class<? extends CellIdentityCdma> cellIdentityCdmaClass = CellIdentityCdma.class;
                                            CellIdentityCdma cellIdentityCdma = cellIdentityCdmaClass.getDeclaredConstructor().newInstance();
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field mNetworkIdField = cellIdentityCdmaClass.getDeclaredField("mNetworkId");
                                            mNetworkIdField.setAccessible(true);
                                            mNetworkIdField.set(cellIdentityCdma, telephoneDto.getCid());
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field mBasestationIdField = cellIdentityCdmaClass.getDeclaredField("mBasestationId");
                                            mBasestationIdField.setAccessible(true);
                                            mBasestationIdField.set(cellIdentityCdma, telephoneDto.getLac());

                                            CellSignalStrengthCdma cellSignalStrengthCdma = CellSignalStrengthCdma.class
                                                    .getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class)
                                                    .newInstance(CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE);

                                            return CellInfoCdma.class.getDeclaredConstructor(
                                                            int.class,
                                                            boolean.class,
                                                            long.class,
                                                            CellIdentityCdma.class,
                                                            CellSignalStrengthCdma.class)
                                                    .newInstance(
                                                            CellInfo.CONNECTION_NONE,
                                                            false,
                                                            Long.MAX_VALUE,
                                                            cellIdentityCdma,
                                                            cellSignalStrengthCdma);
                                        case "TDSCDMA":
                                            Class<? extends CellIdentityTdscdma> cellIdentityTdscdmaClass = CellIdentityTdscdma.class;
                                            CellIdentityTdscdma cellIdentityTdscdma = cellIdentityTdscdmaClass.getDeclaredConstructor().newInstance();
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityTdscdmaMCidField = cellIdentityTdscdmaClass.getDeclaredField("mCid");
                                            cellIdentityTdscdmaMCidField.setAccessible(true);
                                            cellIdentityTdscdmaMCidField.set(cellIdentityTdscdma, telephoneDto.getCid());
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityTdscdmaMLacField = cellIdentityTdscdmaClass.getDeclaredField("mLac");
                                            cellIdentityTdscdmaMLacField.setAccessible(true);
                                            cellIdentityTdscdmaMLacField.set(cellIdentityTdscdma, telephoneDto.getLac());

                                            CellSignalStrengthTdscdma cellSignalStrengthTdscdma =
                                                    CellSignalStrengthTdscdma.class
                                                            .getDeclaredConstructor(int.class, int.class, int.class)
                                                            .newInstance(CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE);

                                            Class<? extends CellInfoTdscdma> cellInfoTdscdmaClass = CellInfoTdscdma.class;
                                            return cellInfoTdscdmaClass.getDeclaredConstructor(
                                                            int.class,
                                                            boolean.class,
                                                            long.class,
                                                            CellIdentityTdscdma.class,
                                                            CellSignalStrengthTdscdma.class)
                                                    .newInstance(
                                                            CellInfo.CONNECTION_NONE,
                                                            false,
                                                            Long.MAX_VALUE,
                                                            cellIdentityTdscdma,
                                                            cellSignalStrengthTdscdma);
                                        case "WCDMA":
                                            Class<? extends CellIdentityWcdma> cellIdentityWcdmaClass = CellIdentityWcdma.class;
                                            CellIdentityWcdma cellIdentityWcdma = cellIdentityWcdmaClass.getDeclaredConstructor().newInstance();
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityWcdmaMLacField = cellIdentityWcdmaClass.getDeclaredField("mLac");
                                            cellIdentityWcdmaMLacField.setAccessible(true);
                                            cellIdentityWcdmaMLacField.set(cellIdentityWcdma, telephoneDto.getLac());
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityWcdmaMCidField = cellIdentityWcdmaClass.getDeclaredField("mCid");
                                            cellIdentityWcdmaMCidField.setAccessible(true);
                                            cellIdentityWcdmaMCidField.set(cellIdentityWcdma, telephoneDto.getCid());

                                            CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                                    CellSignalStrengthWcdma.class
                                                            .getDeclaredConstructor(int.class, int.class, int.class, int.class)
                                                            .newInstance(CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE);

                                            return CellInfoWcdma.class.getDeclaredConstructor(
                                                            int.class,
                                                            boolean.class,
                                                            long.class,
                                                            CellIdentityWcdma.class,
                                                            CellSignalStrengthWcdma.class)
                                                    .newInstance(
                                                            CellInfo.CONNECTION_NONE,
                                                            false,
                                                            Long.MAX_VALUE,
                                                            cellIdentityWcdma,
                                                            cellSignalStrengthWcdma);
                                        case "GSM":
                                            Class<? extends CellIdentityGsm> cellIdentityGsmClass = CellIdentityGsm.class;
                                            CellIdentityGsm cellIdentityGsm = cellIdentityGsmClass.getDeclaredConstructor().newInstance();
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityGsmMLacField = cellIdentityGsmClass.getDeclaredField("mLac");
                                            cellIdentityGsmMLacField.setAccessible(true);
                                            cellIdentityGsmMLacField.set(cellIdentityGsm, telephoneDto.getLac());
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityGsmMCidField = cellIdentityGsmClass.getDeclaredField("mCid");
                                            cellIdentityGsmMCidField.setAccessible(true);
                                            cellIdentityGsmMCidField.set(cellIdentityGsm, telephoneDto.getCid());

                                            CellSignalStrengthGsm cellSignalStrengthGsm =
                                                    CellSignalStrengthGsm.class
                                                            .getDeclaredConstructor(int.class, int.class, int.class)
                                                            .newInstance(CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE);

                                            return CellInfoGsm.class.getDeclaredConstructor(
                                                            int.class,
                                                            boolean.class,
                                                            long.class,
                                                            CellIdentityGsm.class,
                                                            CellSignalStrengthGsm.class)
                                                    .newInstance(CellInfo.CONNECTION_NONE,
                                                            false,
                                                            Long.MAX_VALUE,
                                                            cellIdentityGsm,
                                                            cellSignalStrengthGsm);
                                        case "LTE":
                                            Class<? extends CellIdentityLte> cellIdentityLteClass = CellIdentityLte.class;
                                            CellIdentityLte cellIdentityLte = cellIdentityLteClass.getDeclaredConstructor().newInstance();
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityLteMCiField = cellIdentityLteClass.getDeclaredField("mCi");
                                            cellIdentityLteMCiField.setAccessible(true);
                                            cellIdentityLteMCiField.set(cellIdentityLte, telephoneDto.getCid());
                                            @SuppressLint("SoonBlockedPrivateApi")
                                            Field cellIdentityLteMTacField = cellIdentityLteClass.getDeclaredField("mTac");
                                            cellIdentityLteMTacField.setAccessible(true);
                                            cellIdentityLteMTacField.set(cellIdentityLte, telephoneDto.getLac());

                                            CellSignalStrengthLte cellSignalStrengthLte =
                                                    CellSignalStrengthLte.class
                                                            .getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class, int.class, int.class)
                                                            .newInstance(CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE,
                                                                    CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE, CellInfo.UNAVAILABLE);

                                            Class<?> cellConfigLteClass = Class.forName("android.telephony.CellConfigLte");
                                            Object cellConfigLte = cellConfigLteClass.getDeclaredConstructor(boolean.class).newInstance(false);

                                            return CellInfoLte.class.getDeclaredConstructor(
                                                            int.class,
                                                            boolean.class,
                                                            long.class,
                                                            CellIdentityLte.class,
                                                            CellSignalStrengthLte.class,
                                                            cellConfigLteClass)
                                                    .newInstance(CellInfo.CONNECTION_NONE,
                                                            false,
                                                            Long.MAX_VALUE,
                                                            cellIdentityLte,
                                                            cellSignalStrengthLte,
                                                            cellConfigLte);
                                        case "NR":
                                            Class<CellIdentityNr> cellIdentityNrClass = CellIdentityNr.class;
                                            CellIdentityNr cellIdentityNr = cellIdentityNrClass.getDeclaredConstructor().newInstance();
                                            @SuppressLint("BlockedPrivateApi")
                                            Field cellIdentityNrMPciField = cellIdentityNrClass.getDeclaredField("mPci");
                                            cellIdentityNrMPciField.setAccessible(true);
                                            cellIdentityNrMPciField.set(cellIdentityNr, telephoneDto.getCid());
                                            @SuppressLint("BlockedPrivateApi")
                                            Field cellIdentityNrMTacField = cellIdentityNrClass.getDeclaredField("mTac");
                                            cellIdentityNrMTacField.setAccessible(true);
                                            cellIdentityNrMTacField.set(cellIdentityNr, telephoneDto.getLac());

                                            CellSignalStrengthNr cellSignalStrengthNr =
                                                    CellSignalStrengthNr.class.getDeclaredConstructor().newInstance();

                                            return CellInfoNr.class
                                                    .getDeclaredConstructor(
                                                            int.class,
                                                            boolean.class,
                                                            long.class,
                                                            CellIdentityNr.class,
                                                            CellSignalStrengthNr.class)
                                                    .newInstance(
                                                            CellInfo.CONNECTION_NONE,
                                                            false,
                                                            Long.MAX_VALUE,
                                                            cellIdentityNr,
                                                            cellSignalStrengthNr);
                                        default:
                                            return null;
                                    }
                                } catch (Exception e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    afterHookCallback.setResult(allCellInfo);
                }
            });
        }
    }

}
