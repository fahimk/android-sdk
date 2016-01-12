package com.yesgraph.android;

import android.app.Application;
import android.graphics.Color;
import android.test.ApplicationTestCase;

import com.yesgraph.android.utils.CustomTheme;
import com.yesgraph.android.utils.Utility;

/**
 * Created by Klemen on 17.12.2015.
 */
public class ThemeUnitTest extends ApplicationTestCase<Application> {
    public ThemeUnitTest() {
        super(Application.class);
    }

    /**
     * Validate custom theme color
     */
    public void testValidateCustomThemeColors() {

        CustomTheme theme = new CustomTheme();

        int mainForegroundColor = Color.parseColor("#0078BD");
        int mainBackgroundColor = Color.parseColor("#1078BD");
        int darkFontColor = Color.parseColor("#2078BD");
        int lightFontColor = Color.parseColor("#3078BD");
        int rowSelectedColor = Color.parseColor("#4078BD");
        int rowUnselectedColor = Color.parseColor("#5078BD");
        int rowBackgroundColor = Color.parseColor("#6078BD");
        int backArrowColor = Color.parseColor("#7078BD");
        int copyButtonColor = Color.parseColor("#8078BD");
        int referralBannerBackgroundColor = Color.parseColor("#9078BD");

        theme.setThemeColor(mainForegroundColor, mainBackgroundColor, darkFontColor, lightFontColor, rowSelectedColor, rowUnselectedColor, rowBackgroundColor, backArrowColor, copyButtonColor, referralBannerBackgroundColor);

        boolean isEqualMainForegroundColor = mainForegroundColor == theme.getMainForegroundColor();
        assertEquals(true, isEqualMainForegroundColor);

        boolean isEqualBackForegroundColor = mainBackgroundColor == theme.getMainBackgroundColor();
        assertEquals(true, isEqualBackForegroundColor);

        boolean isEqualDarkFontColor = darkFontColor == theme.getDarkFontColor();
        assertEquals(true, isEqualDarkFontColor);

        boolean isLightFontColor = lightFontColor == theme.getLightFontColor();
        assertEquals(true, isLightFontColor);

        boolean isEqualRowSelectedColor = rowSelectedColor == theme.getRowSelectedColor();
        assertEquals(true, isEqualRowSelectedColor);

        boolean isEqualRowUnSelectedColor = rowUnselectedColor == theme.getRowUnselectedColor();
        assertEquals(true, isEqualRowUnSelectedColor);

        boolean isEqualRowBackgroundColor = rowBackgroundColor == theme.getRowBackgroundColor();
        assertEquals(true, isEqualRowBackgroundColor);

        boolean isEqualBackArrowColor = backArrowColor == theme.getBackArrowColor();
        assertEquals(true, isEqualBackArrowColor);

        boolean isEqualCopyButtonColor = copyButtonColor == theme.getCopyButtonColor();
        assertEquals(true, isEqualCopyButtonColor);

        boolean isEqualReferralBannerBackgroundColor = referralBannerBackgroundColor == theme.getReferralBunnerBackgroundColor();
        assertEquals(true, isEqualReferralBannerBackgroundColor);

    }


    /**
     * Validate custom theme color
     */
    public void testValidateCustomThemeTextSize() {

        String customFonts = "Arial";

        CustomTheme theme = new CustomTheme();
        theme.setFonts(customFonts);

        boolean isEqualFont = customFonts.equals(theme.getFont());

        assertEquals(true, isEqualFont);

    }

    /**
     * Validate custom theme referral text size
     */
    public void testValidateCustomReferralTextSize() {

        int textSize = 22;

        CustomTheme theme = new CustomTheme();
        theme.setReferralTextSize(textSize);

        boolean isEqualTextSize = theme.getReferralTextSize() == textSize;

        assertEquals(true, isEqualTextSize);

    }

    /**
     * Validate custom theme share button shape
     */
    public void testValidateShareButtonShape() {

        String shape = "rectangle";

        CustomTheme theme = new CustomTheme();
        theme.setShareButtonsShape(shape);

        boolean isEqual = theme.getShareButtonsShape().equals(shape);

        assertEquals(true, isEqual);

    }

    public void testCheckShareProperties() {

        int numberOfSuggestedContacts = 3;
        boolean isFacebookSignedIn = true;
        boolean isTwitterSignedIn = true;

        String copyButtonText = "copyButtonText";
        String copyLinkText = "copyLinkText";
        String smsText = "smsText";
        String emailText = "emailText";
        String emailSubject = "emailSubject";
        String shareText = "shareText";

        CustomTheme theme = new CustomTheme();

        theme.setCopyButtonText(copyButtonText);
        theme.setCopyLinkText(copyLinkText);
        theme.setSmsText(smsText);
        theme.setEmailText(emailText);
        theme.setEmailSubject(emailSubject);
        theme.setShareText(shareText);
        theme.setIsFacebookSignedIn(isFacebookSignedIn);
        theme.setIsTwitterSignedIn(isTwitterSignedIn);
        theme.setNumberOfSuggestedContacts(numberOfSuggestedContacts);

        assertEquals(numberOfSuggestedContacts, theme.getNumberOfSuggestedContacts());
        assertEquals(isFacebookSignedIn, theme.isFacebookSignedIn());
        assertEquals(isTwitterSignedIn, theme.isTwitterSignedIn());

        assertEquals(copyButtonText, theme.getCopyButtonText(getContext()));
        assertEquals(copyLinkText, theme.getCopyLinkText(getContext()));

        assertEquals(smsText, theme.getSmsText(getContext()));
        assertEquals(emailText, theme.getEmailText(getContext()));
        assertEquals(emailSubject, theme.getEmailSubject(getContext()));

        assertEquals(shareText, theme.getShareText(getContext()));

    }


}