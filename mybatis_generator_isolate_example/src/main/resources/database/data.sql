insert into `user`(`name`, `age`, `address`, `email`) values
('Jack', '30', '杭州', 'jack@gmail.com'),
('Jack0', '30', '杭州', 'jack@gmail.com'),
('Jack1', '30', '杭州', 'jack@gmail.com'),
('Jack2', '30', '杭州', 'jack@gmail.com'),
('Jack3', '30', '杭州', 'jack@gmail.com'),
('Jack4', '30', '杭州', 'jack@gmail.com'); 
insert into `role`(`name`, `description`) values ('管理员', '管理员');
insert into `user_role`(`user_id`, `role_id`) values (1, 1);