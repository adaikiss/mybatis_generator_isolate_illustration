/**
 * 
 */
package com.example.page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hlw
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PageRequestDefaults {

	/**
	 * The default-size the injected {@link org.springframework.data.domain.Pageable} should get if no corresponding
	 * parameter defined in request (default is {@link PageRequest.DEFAULT_SIZE}).
	 */
	int pageSize() default PageRequest.DEFAULT_SIZE;

	/**
	 * The default-pagenumber the injected {@link org.synyx.hades.domain.Pageable} should get if no corresponding
	 * parameter defined in request (default is 1).
	 */
	int pageNumber() default 1;

	SortDefaults[] sorts() default {};
	public @interface SortDefaults {
		String sort();
		String order();
	}
}
