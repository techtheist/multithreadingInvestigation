DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'account') THEN
        CREATE TABLE IF NOT EXISTS account (
            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
            version BIGINT NOT NULL,
            balance BIGINT NOT NULL
        );

        INSERT INTO account (version, balance)
        SELECT 0, 0
        FROM generate_series(0, 10000);
    END IF;
END $$;