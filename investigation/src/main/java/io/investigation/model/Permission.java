package io.investigation.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tb_permission")
public class Permission extends Model<Permission> {
	private static final long serialVersionUID = 1L;
	private Integer resourceId;
	private Integer parentId;
	private String resourceName;
	private String resourceUrl;
	private String resouceDesc;
	@Override
	protected Serializable pkVal() {
		return this.resourceId;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getResouceDesc() {
		return resouceDesc;
	}
	public void setResouceDesc(String resouceDesc) {
		this.resouceDesc = resouceDesc;
	}

}
