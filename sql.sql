create table person (id text, name text, primary key(id));

create table product (id text, name text, primary key(id));

create table salesman (personid text, phone text, primary key(personid));

create table order_table (orderid text, salesmanid text, productid text, quantity int, primary key(orderid));