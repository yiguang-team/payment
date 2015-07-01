package com.yiguang.payment.depot.vo;

import java.io.Serializable;
import java.util.Date;

import com.yiguang.payment.business.product.vo.PointVO;

public class ProductDepotVO implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private long productId;

	private PointVO pointVO;

	private String cardId;

	private String cardPwd;

	private String batchId;

	private Date usefulStartDate;

	private Date usefulEndDate;

	private Date stockInDate;

	private int status;

	private String statusLabel;

	private String remark;

	private String extractNo;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
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

	public String getBatchId()
	{
		return batchId;
	}

	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}

	public Date getUsefulStartDate()
	{
		return usefulStartDate;
	}

	public void setUsefulStartDate(Date usefulStartDate)
	{
		this.usefulStartDate = usefulStartDate;
	}

	public Date getUsefulEndDate()
	{
		return usefulEndDate;
	}

	public void setUsefulEndDate(Date usefulEndDate)
	{
		this.usefulEndDate = usefulEndDate;
	}

	public Date getStockInDate()
	{
		return stockInDate;
	}

	public void setStockInDate(Date stockInDate)
	{
		this.stockInDate = stockInDate;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getExtractNo()
	{
		return extractNo;
	}

	public void setExtractNo(String extractNo)
	{
		this.extractNo = extractNo;
	}

	public String getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	public PointVO getPointVO()
	{
		return pointVO;
	}

	public void setPointVO(PointVO pointVO)
	{
		this.pointVO = pointVO;
	}

}
