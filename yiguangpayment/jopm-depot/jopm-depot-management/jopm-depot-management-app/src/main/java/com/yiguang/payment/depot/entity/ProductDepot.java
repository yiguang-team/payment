package com.yiguang.payment.depot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.yiguang.payment.business.product.entity.Point;

@Entity
@Table(name = "t_product_depot")
@SequenceGenerator(name = "seq_product_depot", sequenceName = "seq_product_depot")
public class ProductDepot implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product_depot")
	@Column(name = "id")
	private long id;

	@Column(name = "product_id")
	private long productId;

	@ManyToOne
	@JoinColumn(name = "charging_point_id", nullable = false)
	private Point point;

	@Column(name = "card_id")
	private String cardId;

	@Column(name = "card_pwd")
	private String cardPwd;

	@Column(name = "batch_id")
	private String batchId;

	@Column(name = "useful_start_date")
	private Date usefulStartDate;

	@Column(name = "useful_end_date")
	private Date usefulEndDate;

	@Column(name = "stock_in_date")
	private Date stockInDate;

	@Column(name = "status")
	private int status;

	@Column(name = "remark")
	private String remark;

	@Column(name = "extract_no")
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

	public Point getPoint()
	{
		return point;
	}

	public void setPoint(Point point)
	{
		this.point = point;
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
}
