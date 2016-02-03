package com.chencoder.utils;

/**
 * �ο�BigDecimal, �����õ�StringBuilder, ��ԼStringBuilder�ڲ���char[]
 * 
 * �ο������ʾ����뽫�䱣��ΪThreadLocal.
 * 
 * <pre>
 * private static final ThreadLocal<StringBuilderHelper> threadLocalStringBuilderHolder = new ThreadLocal<StringBuilderHelper>() {
 * 	&#64;Override
 * 	protected StringBuilderHelper initialValue() {
 * 		return new StringBuilderHelper(256);
 * 	}
 * };
 * 
 * StringBuilder sb = threadLocalStringBuilderHolder.get().resetAndGetStringBuilder();
 * 
 * </pre>
 * 
 * @author calvin
 *
 */
public class StringBuilderHolder {

	private final StringBuilder sb;

	public StringBuilderHolder(int capacity) {
		sb = new StringBuilder(capacity);
	}


	public StringBuilder resetAndGetStringBuilder() {
		sb.setLength(0);
		return sb;
	}
}