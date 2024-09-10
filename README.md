# 找队友平台

帮助想要组队的小伙伴找到理想的队友

项目开源

主要工作：

1. jwt认证
2. 统一返回值
3. 全局异常处理器（实验得到：与sentinel配合使用时，如果触发降级或者blockException则不生效）
4. 流式编程、lambda表达式
5. nacos服务注册与发现，分布式配置中心
6. openfeign服务调用、负载均衡
7. sentinel服务熔断、降级、监控
8. gateway服务网关
9. redisson分布式锁，防止注册相同账号
10. knife4j、swagger接口文档
11. 布隆过滤器
12. rocketmq
13. seata分布式事务
14. micrometer tracing服务链路追踪

## 1 需求分析

- 用户
  - 注册
  - 登录、注销
  - 修改标签
- 添加标签（管理员）
- 搜索
  - 根据用户名搜索用户所有信息（包括标签）
- 推荐
  - 根据个人标签推荐
- 给指定用户发送消息（redis，分布式事务：消息表，mq）
- 查看总发送消息数量（分布式事务：消息数量表，mq）
- 查看历史消息记录（mq，定时任务清除数据）

## 2 设计

### 2.1 数据库表设计

#### 2.1.1 **标签表**

label

| 字段        | 类型     | 备注             |
| ----------- | -------- | ---------------- |
| label_id    | int      | 主键             |
| label_name  | varchar  | 索引，非空，唯一 |
| create_time | datetime |                  |
| update_time | datetime |                  |
| is_delete   | tinyint  |                  |

#### 2.1.2 **用户表**

user

| 字段        | 类型     | 备注                               |
| ----------- | -------- | ---------------------------------- |
| user_id     | int      | 主键                               |
| user_name   | varchar  | 索引，非空，唯一                   |
| password    | varchar  |                                    |
| is_admin    | tinyint  | 0普通用户  1管理员                 |
| nickname    | varchar  | 昵称                               |
| gender      | tinyint  | 0-未知 1-男 2-女                   |
| grade       | tinyint  | 0-未知 1-4 大一-大四 5研一 10-毕业 |
| create_time | datetime |                                    |
| update_time | datetime |                                    |
| is_delete   | tinyint  |                                    |

#### 2.1.3 用户-标签表

user_label

| 字段        | 类型     | 备注       |
| ----------- | -------- | ---------- |
| id          | int      | 主键       |
| user_id     | int      | 非空，索引 |
| label_id    | int      | 非空，索引 |
| create_time | datetime |            |
| update_time | datetime |            |
| is_delete   | tinyint  |            |

#### 2.1.4 消息表

message

| 字段             | 类型         | 备注     |
| ---------------- | ------------ | -------- |
| id               | int          | 主键     |
| send_username    | varchar(30)  | 索引     |
| receive_username | varchar(30)  | 索引     |
| content          | varchar(100) | 消息内容 |
| send_time        | datetime     |          |
| is_delete        | tinyint      |          |

#### 2.1.5消息数量表

message_num

| 字段        | 类型        | 备注             |
| ----------- | ----------- | ---------------- |
| id          | int         | 主键             |
| username    | varchar(30) | 唯一，非空，索引 |
| num         | int         | 消息数量         |
| update_time | datetime    |                  |
| is_delete   | tinyint     |                  |

## 3 技术栈

后端

![image-20240826093052184](C:\Users\zzy\AppData\Roaming\Typora\typora-user-images\image-20240826093052184.png)

![image-20240826093133186](C:\Users\zzy\AppData\Roaming\Typora\typora-user-images\image-20240826093133186.png)

![image-20240826094356295](C:\Users\zzy\AppData\Roaming\Typora\typora-user-images\image-20240826094356295.png)