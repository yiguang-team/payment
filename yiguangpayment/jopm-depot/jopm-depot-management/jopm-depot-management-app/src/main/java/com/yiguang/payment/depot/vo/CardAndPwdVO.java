package com.yiguang.payment.depot.vo;

import java.io.Serializable;

public class CardAndPwdVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String cardId;

	private String cardPwd;

	public CardAndPwdVO()
	{

	}

	public CardAndPwdVO(String cardId, String cardPwd)
	{
		super();
		this.cardId = cardId;
		this.cardPwd = cardPwd;
	}

	public String getCardId()
	{
		return cardId;
	}

	public void setCardId(String cardId)
	{
		this.cardId = cardId;
	}

	public String getCardPwd()
	{
		return cardPwd;
	}

	public void setCardPwd(String cardPwd)
	{
		this.cardPwd = cardPwd;
	}
}
