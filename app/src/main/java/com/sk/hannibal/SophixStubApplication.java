package com.sk.hannibal;

/**
 * |                   quu..__
 * |                    $$  `---.__
 * |                     "$        `--.                          ___.---uuudP
 * |                      `$           `.__.------.__     __.---'      $$$$"              .
 * |                        "          -'            `-.-'            $$$"              .'|
 * |                          ".                                       d$"             _.'  |
 * |                            `.   /                              ..."             .'     |
 * |                              `./                           ..::-'            _.'       |
 * |                               /                         .:::-'            .-'         .'
 * |                              :                          ::''\          _.'            |
 * |                             .' .-.             .-.           `.      .'               |
 * |                             : /'$$|           .@"$\           `.   .'              _.-'
 * |                            .'|$$|          |$$,$$|           |  <            _.-'
 * |                            | `:$$:'          :$$$$$:           `.  `.       .-'
 * |                            :                  `"--'             |    `-.     \
 * |                           :$$.       ==             .$$$.       `.      `.    `\
 * |                           |$$:                      :$$$:        |        >     >
 * |                           |$'     `..'`..'          `$$$'        x:      /     /
 * |                            \                                   xXX|     /    ./
 * |                             \                                xXXX'|    /   ./
 * |                             /`-.                                  `.  /   /
 * |                            :    `-  ...........,                   | /  .'
 * |                            |         ``:::::::'       .            |<    `.
 * |                            |             ```          |           x| \ `.:``.
 * |                            |                         .'    /'   xXX|  `:`M`M':.
 * |                            |    |                    ;    /:' xXXX'|  -'MMMMM:'
 * |                            `.  .'                   :    /:'       |-'MMMM.-'
 * |                             |  |                   .'   /'        .'MMM.-'
 * |                             `'`'                   :  ,'          |MMM<
 * |                               |                     `'            |tbap\
 * |                                \                                  :MM.-'
 * |                                 \                 |              .''
 * |                                  \.               `.            /
 * |                                   /     .:::::::.. :           /
 * |                                  |     .:::::::::::`.         /
 * |                                  |   .:::------------\       /
 * |                                 /   .''               >::'  /
 * |                                 `',:                 :    .'
 * |
 * |                                                      `:.:'
 * |
 * |
 * |
 *
 * @author SK on 2020/8/7
 */


import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Sophix?????????????????????????????????Sophix????????????????????????????????????
 * ?????????????????????SophixApplication???onCreate????????????????????????
 * ????????????????????????????????????????????????????????????????????????????????????????????????
 * AndroidManifest?????????application???????????????SophixEntry???????????????Application??????
 * ????????????Application??????????????????????????????Sophix?????????????????????????????????Application??????
 * ???????????????????????????????????????????????????????????????
 */
public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";
    // ??????SophixEntry??????????????????Application???????????????RealApplicationStub?????????????????????
    @Keep
    @SophixEntry(AppApplication.class)
    static class RealApplicationStub {}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         ??????????????????MultiDex???????????????????????????
         MultiDex.install(this);
        initSophix();
    }
    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setEnableDebug(true)
                .setEnableFullLog()
                .setSecretMetaData("30962303","5e8f2039d515ea12fb25dcea23f1fed8","MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFrpActlBzV1A1ZeZylzFTGGNxOJAROaPMYRSFc/irDMBE4Xx7dwphSn9cXe/UbSR/1NH691zd6GVn19TSUa1olptIv1JVuEDKP92sUhwRgvpI1iOmMoLmsEJY7aOS4Vu8vVLeiiMXyTFrC6FHhijnZu+7wiEDjA0jdIs8yTds+YXBdV6AsSum+Y0Pk7vJXD2LvKGvpy6kYQDfktpPlqBqwnIrCyQLsKtxZeD+RLzkqqslnkeSKMHRgpIyqiszTOuDogmLP26fFBoTffin0JsRSEwXL6QUb04v1qIKquF68lqFshh0vhCZkyKgsHsTS5UvXcf0GY+4wMhOUkYu0UGdAgMBAAECggEAYfgdWz1tViot+QQ1z9Ln7grYjdgzFLyWQyj+XhMwsUKGAbTygpUqAUUUc3/QTHC0KA6y2GbCI0q2TlUJfh0npZeCM90qnPGMYexOK4R9V4QSoqB0vc8UybVw2jVGBFWt4nMpMTgxIbRryBRc3MsAXR2+IW7SvQO9eTdm92RLf0uLP/cL8i3m0QMTT3ziiVHPUO3Sih2tU8fE+stxYTLp5x+okslwaVo3B1lSEhBaTaNaI047bBOIlr3S/DFFtozc0YY+Tg7tcvEabpve9uw6td90U8hZrLzuyix5TTFIm8qgFCDgRvNsPUr8R80TNtJQfxneaGSBBURQ6kFvuQpc4QKBgQC8D97q+lO2R26EloeCEDVvmwdAYOKU1B9EMgHJy+5RWl4Bi5JR3fwRS8lBE6NacZVwRxaCKd06ayBDgOo9bNKSu/g5Bm68nEeF+RgAMqDWC21l/jUE117BYiPP8qRlv/2PwFC8dq0MRhmAS5aZObraHQnymXCCXWiMeFYXArVBWQKBgQC1+ZMavBEYDzPv9OcNmtUBUZEMLOpPHAoxdTATq++Aq9N72eJ1HEygJqTc3/6ckxDA/MV9bYuhcP/YfmBwufnJr4c70VVEv+iaLlTSDNEOMw8TAKS20uVukg74cMIHnQbBV+ZoT67Jh/cC5NYcGQ9njqJpnXwFa+dNvJcvHy2V5QKBgFS7cdy9XOpL1Oe4SuW3F9xpJSNEg9I6oLvBLD0XvRC6fpCDqjFoqyG/27WOe5NMieBEp0eORTcE/Uxs0R1L4rRqLLTEyTAB2dfrG307UmweSfQiTREZWIAZD7mpwZh2oHAcXkQL1HwrOWIsrHXtJNsRKmI78A1MuMZRkbhcZ/5BAoGANqc9UiM3sxUl0SSpnEFrRdkcih6XRKk0OoRqqw3RgcCZZLIcvHWCYfsZOqkfjCDzQDLqkXAsJpGMlZXnGxQP/E32fLWzOVG151CQ8wEcbHpTRa7EyY/vjlTZoC4laCn2FG106tE7upVWT40w7QFtyflht/yveeVdc9B3S5S+mS0CgYBSvdy2MwhS/Mk5//0U329fp/rFGjvI2T4hm1R3lpu+YGqdZ1cuhcgQSkwpeHYkZ5G6Sy9D5FZ9fankcwuT0aZtioJ5T/Lzy7A/y1N8oL28075DgFeVjsxngeMo9dqlfirHbL+RDrYo9plwvAoy3UQ1qzWrM51T1zz8gjSjvzgRdQ==")
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        Log.e(TAG, "sophix load patch! mode:" + mode + " code:" + code + " info:" + info + " handlePatchVersion:" + handlePatchVersion);
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // ?????????????????????????????????????????????SharePreference???????????????
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }



}
