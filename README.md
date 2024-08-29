# 找队友平台

帮助想要组队的小伙伴找到理想的队友

项目开源

主要工作：

1. jwt认证
2. 统一返回值
3. 全局异常处理器
4. 拦截器
5. 流式编程、lambda表达式
6. nacos服务注册与发现，分布式配置中心
7. openfeign服务调用、负载均衡
8. seata分布式事务
9. sentinel服务熔断、降级、监控
10. micrometer tracing服务链路追踪
11. gateway服务网关
12. redisson分布式锁，防止注册相同账号
13. docker部署
14. knife4j、swagger接口文档
15. 布隆过滤器

## 1 需求分析

- 用户
  - 注册
  - 登录、注销
  - 修改标签
- 添加标签（管理员）
- 搜索
  - 根据标签搜索用户
  - 根据id搜索用户
  - 根据标签搜索队伍
- 队伍
  - 创建、解散队伍
  - 加入、退出队伍
  - 修改队伍标签
- 推荐
  - 根据队伍标签推荐
  - 根据个人标签推荐

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
| user_id     | int      | 非空       |
| label_id    | int      | 非空，索引 |
| create_time | datetime |            |
| update_time | datetime |            |
| is_delete   | tinyint  |            |

#### 2.1.4 队伍表

team

| 字段           | 类型     | 备注       |
| -------------- | -------- | ---------- |
| team_id        | int      | 主键       |
| team_name      | varchar  |            |
| label_class_id | int      | 所属类别id |
| create_time    | datetime |            |
| update_time    | datetime |            |
| is_delete      | tinyint  |            |

### 2.2 项目结构

find-teammate

​		public-module

​		user-module

​		admin-module

​		serch-module

​		team-module

​		recommend-module

## 3 技术栈

后端

![image-20240826093052184](C:\Users\zzy\AppData\Roaming\Typora\typora-user-images\image-20240826093052184.png)

![image-20240826093133186](C:\Users\zzy\AppData\Roaming\Typora\typora-user-images\image-20240826093133186.png)

![image-20240826094356295](C:\Users\zzy\AppData\Roaming\Typora\typora-user-images\image-20240826094356295.png)