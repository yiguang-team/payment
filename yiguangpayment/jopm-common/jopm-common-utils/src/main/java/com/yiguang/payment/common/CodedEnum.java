package com.yiguang.payment.common;

import java.io.Serializable;

public interface CodedEnum<T> extends Serializable
{
	String getCode(T t);

	T getEnum(String code);
}
