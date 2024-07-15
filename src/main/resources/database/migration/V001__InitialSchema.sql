CREATE TABLE IF NOT EXISTS employee (
    id binary(16) NOT NULL,
    full_name VARCHAR(255) not null,
    remaining_vacation_days INTEGER DEFAULT 30 NOT NULL,
    title enum ('MANAGER','WORKER') NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS vacation_request (
    id binary(16) NOT NULL,
    employee_id binary(16) NOT NULL,
    worker_id binary(16) NOT NULL,
    status ENUM ('APPROVED','PENDING','REJECTED') NOT NULL,
    manager_id binary(16) NOT NULL,
    requested_days INTEGER,
    request_created_at datetime(6),
    vacation_start_date datetime(6),
    vacation_end_date datetime(6),
    primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE vacation_request
    ADD CONSTRAINT fk_vacation_request_employee_id
    FOREIGN KEY (employee_id)
    REFERENCES employee (id)
