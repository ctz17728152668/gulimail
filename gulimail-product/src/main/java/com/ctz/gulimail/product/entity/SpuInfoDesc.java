package com.ctz.gulimail.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * spu信息介绍
 * @TableName pms_spu_info_desc
 */
@TableName(value ="pms_spu_info_desc")
@Data
public class SpuInfoDesc implements Serializable {
    /**
     * 商品id
     */
    @TableId(value = "spu_id",type = IdType.INPUT)
    private Long spuId;

    /**
     * 商品介绍
     */
    @TableField(value = "decript")
    private String decript;

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
        SpuInfoDesc other = (SpuInfoDesc) that;
        return (this.getSpuId() == null ? other.getSpuId() == null : this.getSpuId().equals(other.getSpuId()))
            && (this.getDecript() == null ? other.getDecript() == null : this.getDecript().equals(other.getDecript()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuId() == null) ? 0 : getSpuId().hashCode());
        result = prime * result + ((getDecript() == null) ? 0 : getDecript().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spuId=").append(spuId);
        sb.append(", decript=").append(decript);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}