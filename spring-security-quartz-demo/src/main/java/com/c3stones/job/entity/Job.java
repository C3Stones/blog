package com.c3stones.job.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 定时任务
 * 
 * @author CL
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_sys_job")
@EqualsAndHashCode(callSuper = false)
public class Job extends Model<Job> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * cron表达式
	 */
	private String cronExpression;

	/**
	 * 任务执行类（包名+类名）
	 */
	private String beanClass;

	/**
	 * 任务状态（0-停止，1-运行）
	 */
	private String status;

	/**
	 * 任务分组
	 */
	private String jobGroup;

	/**
	 * 参数
	 */
	private String jobDataMap;

	/**
	 * 下一次执行时间
	 */
	@TableField(exist = false)
	private LocalDateTime nextfireDate;

	/**
	 * 创建人ID
	 */
	private Integer createUserId;

	/**
	 * 创建时间
	 */
	private LocalDateTime createDate;

	/**
	 * 更新人ID
	 */
	private Integer updateUserId;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateDate;

	/**
	 * 描述
	 */
	private String remarks;

}