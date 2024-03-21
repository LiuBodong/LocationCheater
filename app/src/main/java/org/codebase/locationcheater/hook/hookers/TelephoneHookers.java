package org.codebase.locationcheater.hook.hookers;

import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
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
                                            Class<? extends CellInfoCdma> cellInfoCdmaClass = CellInfoCdma.class;
                                            Constructor<? extends CellInfoCdma> cellInfoCdmaConstructor = cellInfoCdmaClass.getDeclaredConstructor();
                                            Method setCdmaCellIdentityMethod = cellInfoCdmaClass.getDeclaredMethod("setCellIdentity", CellIdentityCdma.class);
                                            setCdmaCellIdentityMethod.setAccessible(true);
                                            CellInfoCdma cellInfoCdma = cellInfoCdmaConstructor.newInstance();
                                            Class<? extends CellIdentityCdma> cellIdentityCdmaClass = CellIdentityCdma.class;
                                            CellIdentityCdma cellIdentityCdma = cellIdentityCdmaClass.getDeclaredConstructor().newInstance();
                                            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                Field mNetworkId = cellIdentityCdmaClass.getDeclaredField("mNetworkId");
                                            }
                                            setCdmaCellIdentityMethod.invoke(cellInfoCdma, cellIdentityCdma);
                                            return cellInfoCdma;
                                        case "TDSCDMA":
                                        case "WCDMA":
                                        case "GSM":
                                        case "LTE":
                                        case "NR":
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
