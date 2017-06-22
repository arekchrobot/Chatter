package pl.ark.chr.simplechat.rest.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.*;

/**
 * Created by Arek on 2017-06-22.
 */
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ResponseStatus(HttpStatus.OK)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GET {

    @AliasFor(annotation = RequestMapping.class, attribute = "name") String name() default "";

    @AliasFor(annotation = RequestMapping.class, attribute = "path") String[] value() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "path") String[] path() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "params") String[] params() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "headers") String[] headers() default {};
}
