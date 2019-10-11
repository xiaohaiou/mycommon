package zhu.liang.common.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class HibernateValidatorUtil {
	private static Validator validator;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	public static <T> ValidateResult validator(T object) {
		if (null == object) {
			return new ValidateResult(false,
					new String[]{"The object to be validated must not be null."});
		}

		Set<ConstraintViolation<T>> violations = validator.validate(object);
		int errSize = violations.size();

		String[] errMsg = new String[errSize];
		boolean result = true;
		if (errSize > 0) {
			int i = 0;
			for (ConstraintViolation<T> violation : violations) {
				errMsg[i] = violation.getMessage();
				i++;
			}
			result = false;
		}

		return new ValidateResult(result, errMsg);
	}

}
