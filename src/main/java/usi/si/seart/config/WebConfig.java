package usi.si.seart.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import usi.si.seart.converter.GitRepoToDtoConverter;
import usi.si.seart.converter.JsonObjectToErrorResponseConverter;
import usi.si.seart.converter.JsonObjectToGitCommitConverter;
import usi.si.seart.converter.JsonObjectToGitRepoMetricConverter;
import usi.si.seart.converter.SearchParameterDtoToGitRepoSearchConverter;
import usi.si.seart.converter.StringToContactsConverter;
import usi.si.seart.converter.StringToGitExceptionConverter;
import usi.si.seart.converter.StringToLicensesConverter;
import usi.si.seart.converter.StringToNavigationLinksConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NotNull final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET")
                .allowedOrigins(
                        "http://localhost:3030",
                        "http://localhost:7030",
                        "https://seart-ghs.si.usi.ch"
                )
                .exposedHeaders(
                        "X-Link-Search",
                        "X-Link-Download"
                );
    }

    @Override
    public void addFormatters(@NotNull final FormatterRegistry registry) {
        registry.addConverter(new GitRepoToDtoConverter());
        registry.addConverter(new JsonObjectToGitCommitConverter());
        registry.addConverter(new JsonObjectToErrorResponseConverter());
        registry.addConverter(new JsonObjectToGitRepoMetricConverter());
        registry.addConverter(new SearchParameterDtoToGitRepoSearchConverter());
        registry.addConverter(new StringToContactsConverter());
        registry.addConverter(new StringToGitExceptionConverter());
        registry.addConverter(new StringToLicensesConverter());
        registry.addConverter(new StringToNavigationLinksConverter());
    }
}
