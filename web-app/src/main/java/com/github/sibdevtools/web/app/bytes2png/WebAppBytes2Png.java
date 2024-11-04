package com.github.sibdevtools.web.app.bytes2png;

import com.github.sibdevtools.localization.api.dto.LocalizationId;
import com.github.sibdevtools.localization.api.dto.LocalizationSourceId;
import com.github.sibdevtools.localization.mutable.api.source.LocalizationJsonSource;
import com.github.sibdevtools.webapp.api.dto.HealthStatus;
import com.github.sibdevtools.webapp.api.dto.WebApplication;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.github.sibdevtools.web.app.bytes2png.constant.Constants.SYSTEM_CODE;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Component
@LocalizationJsonSource(
        systemCode = SYSTEM_CODE,
        path = "classpath:/web/app/bytes2png/content/localizations/eng.json",
        iso3Code = "eng"
)
@LocalizationJsonSource(
        systemCode = SYSTEM_CODE,
        path = "classpath:/web/app/bytes2png/content/localizations/rus.json",
        iso3Code = "rus"
)
public class WebAppBytes2Png implements WebApplication {
    private static final LocalizationSourceId LOCALIZATION_SOURCE_ID = new LocalizationSourceId(SYSTEM_CODE);

    @Nonnull
    @Override
    public String getCode() {
        return "web.app.bytes2png";
    }

    @Nonnull
    @Override
    public String getFrontendUrl() {
        return "/web/app/bytes2png/ui/";
    }

    @Nonnull
    @Override
    public LocalizationId getIconCode() {
        return new LocalizationId(LOCALIZATION_SOURCE_ID, "web.app.bytes2png.icon");
    }

    @Nonnull
    @Override
    public LocalizationId getTitleCode() {
        return new LocalizationId(LOCALIZATION_SOURCE_ID, "web.app.bytes2png.title");
    }

    @Nonnull
    @Override
    public LocalizationId getDescriptionCode() {
        return new LocalizationId(LOCALIZATION_SOURCE_ID, "web.app.bytes2png.description");
    }

    @Nonnull
    @Override
    public Set<String> getTags() {
        return Set.of(
                "converter",
                "bytes2png"
        );
    }

    @Nonnull
    @Override
    public HealthStatus getHealthStatus() {
        return HealthStatus.UP;
    }
}
