package com.ctz.gulimail.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 品牌分类关联
 * @TableName pms_category_brand_relation
 */
@TableName(value ="pms_category_brand_relation")
@Data
public class CategoryBrandRelationEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌id
     */
    @TableField(value = "brand_id")
    private Long brandId;

    /**
     * 分类id
     */
    @TableField(value = "catelog_id")
    private Long catelogId;

    /**
     * 
     */
    @TableField(value = "brand_name")
    private String brandName;

    /**
     * 
     */
    @TableField(value = "catelog_name")
    private String catelogName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CategoryBrandRelationEntity other = (CategoryBrandRelationEntity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBrandId() == null ? other.getBrandId() == null : this.getBrandId().equals(other.getBrandId()))
            && (this.getCatelogId() == null ? other.getCatelogId() == null : this.getCatelogId().equals(other.getCatelogId()))
            && (this.getBrandName() == null ? other.getBrandName() == null : this.getBrandName().equals(other.getBrandName()))
            && (this.getCatelogName() == null ? other.getCatelogName() == null : this.getCatelogName().equals(other.getCatelogName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBrandId() == null) ? 0 : getBrandId().hashCode());
        result = prime * result + ((getCatelogId() == null) ? 0 : getCatelogId().hashCode());
        result = prime * result + ((getBrandName() == null) ? 0 : getBrandName().hashCode());
        result = prime * result + ((getCatelogName() == null) ? 0 : getCatelogName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", brandId=").append(brandId);
        sb.append(", catelogId=").append(catelogId);
        sb.append(", brandName=").append(brandName);
        sb.append(", catelogName=").append(catelogName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}