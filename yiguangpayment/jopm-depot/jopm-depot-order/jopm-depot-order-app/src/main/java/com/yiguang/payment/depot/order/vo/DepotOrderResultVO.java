package com.yiguang.payment.depot.order.vo;

import java.util.List;

import com.yiguang.payment.depot.vo.CardAndPwdVO;

public class DepotOrderResultVO
{

	private String code;

	private String message;

	private List<CardAndPwdVO> content;

	public DepotOrderResultVO()
	{

	}

	public DepotOrderResultVO(String code, String message, List<CardAndPwdVO> cardAndPwds)
	{
		super();
		this.content = cardAndPwds;
		this.message = message;
		this.code = code;
	}

	public List<CardAndPwdVO> getContent()
	{
		return content;
	}

	public void setContent(List<CardAndPwdVO> cardAndPwds)
	{
		this.content = cardAndPwds;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

}
