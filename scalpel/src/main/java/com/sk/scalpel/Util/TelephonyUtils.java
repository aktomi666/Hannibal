package com.sk.scalpel.Util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

public class TelephonyUtils {

    private static TelephonyUtils telephonyUtils;

    private String imsiSIM1;
    private String imsiSIM2;

    private boolean isSIM1Ready;
    private boolean isSIM2Ready;

    public String getImsi(int simCardIndex) {
        if (simCardIndex == 1 || simCardIndex == 0) {
            return imsiSIM1;
        } else {
            return imsiSIM2;
        }
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    public boolean isDualSIM() {
        return isSIM2Ready() && !TextUtils.isEmpty(getImsi(2));
    }

    private TelephonyUtils() { }

    public static TelephonyUtils getInstance(Context context) {

        if (telephonyUtils == null) {
            telephonyUtils = new TelephonyUtils();

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            telephonyUtils.imsiSIM1 = telephonyManager.getSubscriberId();
            telephonyUtils.imsiSIM2 = null;

            try {

                if (TextUtils.isEmpty(telephonyUtils.imsiSIM1)) {
                    telephonyUtils.imsiSIM1 = getIMSIBySlot(context, "getSubscriberId", 0);
                }
                telephonyUtils.imsiSIM2 = getIMSIBySlot(context, "getSubscriberIdGemini", 1);

            } catch (GeminiMethodNotFoundException e) {
                try {
                    if (TextUtils.isEmpty(telephonyUtils.imsiSIM1)) {
                        telephonyUtils.imsiSIM1 = getIMSIBySlot(context, "getSubscriberId", 0);
                    }

                    if (TextUtils.isEmpty(telephonyUtils.imsiSIM2)) {
                        telephonyUtils.imsiSIM2 = getIMSIBySlot(context, "getSubscriberIdGemini", 1);
                    }
                } catch (GeminiMethodNotFoundException e1) {
                }
            }

            telephonyUtils.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
            telephonyUtils.isSIM2Ready = false;

            try {
                if (!telephonyUtils.isSIM1Ready) {
                    telephonyUtils.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
                }

                telephonyUtils.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);

            } catch (GeminiMethodNotFoundException e) {
                try {
                    if (!telephonyUtils.isSIM1Ready) {
                        telephonyUtils.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                    }

                    if (!telephonyUtils.isSIM2Ready) {
                        telephonyUtils.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
                    }
                } catch (GeminiMethodNotFoundException e1) {

                }
            }

            if (TextUtils.isEmpty(telephonyUtils.imsiSIM1)) {
                telephonyUtils.imsiSIM1 = telephonyManager.getDeviceId();
            }
        }

        return telephonyUtils;
    }

    private static String getIMSIBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        String imsi = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                imsi = ob_phone.toString();
            }
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imsi;
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        boolean isReady = false;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);
            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                if (simState == TelephonyManager.SIM_STATE_READY) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    private static class GeminiMethodNotFoundException extends Exception {
        private static final long serialVersionUID = -996812356902545308L;
        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }
}
