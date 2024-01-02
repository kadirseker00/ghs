package ch.usi.si.seart.converter;

import ch.usi.si.seart.exception.git.CheckoutException;
import ch.usi.si.seart.exception.git.CompressionException;
import ch.usi.si.seart.exception.git.GitException;
import ch.usi.si.seart.exception.git.RepositoryDisabledException;
import ch.usi.si.seart.exception.git.RepositoryLockedException;
import ch.usi.si.seart.exception.git.RepositoryNotFoundException;
import ch.usi.si.seart.exception.git.TerminalPromptsDisabledException;
import ch.usi.si.seart.exception.git.config.InvalidProxyConfigurationException;
import ch.usi.si.seart.exception.git.config.InvalidUsernameException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class StringToGitExceptionConverter implements Converter<String, GitException> {

    private static final String LOCKED = "is disabled";
    private static final String NOT_FOUND = "not found";
    private static final String EARLY_EOF = "early EOF";
    private static final String LONG_FILE_NAME = "Filename too long";
    private static final String FORBIDDEN = "The requested URL returned error: 403";
    private static final String DISABLED = "Access to this repository has been disabled by GitHub staff";
    private static final String UNRESOLVABLE_HOST = "Could not resolve host: github.com";
    private static final String CHECKOUT_FAILED = "unable to checkout working tree";
    private static final String AUTHENTICATION_REQUIRED =
            "could not read Username for 'https://github.com': No such device or address";
    private static final String PROMPTS_DISABLED =
            "could not read Username for 'https://github.com': terminal prompts disabled";

    @Override
    @NonNull
    public GitException convert(@NonNull String source) {
        String remote = source.lines()
                .filter(line -> line.startsWith("remote:"))
                .findFirst()
                .map(string -> string.substring(8))
                .orElse("");
        String fatal = source.lines()
                .filter(line -> line.startsWith("fatal:"))
                .findFirst()
                .map(string -> string.substring(7))
                .orElse("");
        String error = source.lines()
                .filter(line -> line.startsWith("error:"))
                .findFirst()
                .map(string -> string.substring(7))
                .orElse("");
        if (fatal.endsWith(NOT_FOUND))
            return new RepositoryNotFoundException(fatal);
        if (fatal.endsWith(FORBIDDEN) && remote.startsWith(DISABLED))
            return new RepositoryDisabledException(fatal);
        if (fatal.endsWith(FORBIDDEN) && remote.contains(LOCKED))
            return new RepositoryLockedException(fatal);
        if (fatal.endsWith(UNRESOLVABLE_HOST))
            return new InvalidProxyConfigurationException(fatal);
        if (fatal.endsWith(LONG_FILE_NAME))
            return new CheckoutException(fatal);
        return switch (fatal) {
            case "" -> new GitException("\n" + source + "\n");
            case EARLY_EOF -> new CompressionException(fatal);
            case PROMPTS_DISABLED -> new TerminalPromptsDisabledException(fatal);
            case AUTHENTICATION_REQUIRED -> new InvalidUsernameException(fatal);
            case CHECKOUT_FAILED -> new CheckoutException(error);
            default -> new GitException(fatal);
        };
    }
}
