# Backup and Recovery Documentation

This document defines backup, retention, and recovery procedures for the application and its PostgreSQL database. It includes verification and testing guidance.

## Scope
- PostgreSQL database(s) backing the application
- Application artifacts (JAR, Docker image, configuration)
- Uploaded files (CAD/images) if stored on filesystem or object storage

## Backup Strategy
- Type: Logical backups via `pg_dump` for per-database, `pg_dumpall` for global objects; optionally add PITR with WAL archiving for large deployments.
- Frequency: Nightly full backups; optional hourly WAL shipping for PITR.
- Retention: 7 daily, 4 weekly, 12 monthly (adjust to compliance).
- Storage: Offsite and cross-region storage (e.g., S3/Blob) with encryption at rest and in transit.

## PostgreSQL Logical Backup

Environment variables (example):
- `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`

Command examples:
```
# Full database backup
pg_dump -Fc -Z 6 -h $PGHOST -p 5432 -U $PGUSER $PGDATABASE \
  -f backup/${PGDATABASE}_$(date +%F).dump

# Global objects (roles/tablespaces)
pg_dumpall -g -h $PGHOST -p 5432 -U $PGUSER \
  > backup/global_$(date +%F).sql
```

Restore:
```
# Create empty database first
createdb -h $PGHOST -U $PGUSER $PGDATABASE

# Restore custom format dump
pg_restore -h $PGHOST -U $PGUSER -d $PGDATABASE --no-owner --role=$PGUSER \
  backup/${PGDATABASE}_2024-01-01.dump

# Restore global objects if needed
psql -h $PGHOST -U $PGUSER -f backup/global_2024-01-01.sql postgres
```

## Point-in-Time Recovery (optional)
- Enable WAL archiving in `postgresql.conf`:
  - `archive_mode=on`, `archive_command='test ! -f /archive/%f && cp %p /archive/%f'`
- Base backup via `pg_basebackup`:
```
pg_basebackup -h $PGHOST -U replicator -D /backups/base/$(date +%F) -Ft -z -Xs
```
- PITR restore: copy base backup, restore WAL up to target time using `recovery.signal` and `restore_command`.

## Application Artifacts Backup
- JAR: `target/3GAssociates-0.0.1-SNAPSHOT.jar`
- Docker image: pushed to a registry (`docker save` as fallback)
- Config: `src/main/resources/application.properties` and environment (.env, compose file)
- Scripts: `docs/env-setup.*`

Commands:
```
# Save docker image
docker build -t 3gassociates:latest .
docker save 3gassociates:latest | gzip > backup/3gassociates_latest.tar.gz
```

## Uploaded Files
- If files are stored on disk, backup the storage directory incrementally.
- If using object storage (recommended), rely on bucket lifecycle/versioning policies.

## Verification
- Run `pg_restore -l` to list dump contents.
- Restore to a staging database weekly and run smoke tests.
- Validate row counts for key tables vs. production (within expected deltas).

## Disaster Recovery Runbook
1. Provision a clean PostgreSQL instance.
2. Restore most recent successful dump (or PITR to desired timestamp).
3. Apply necessary schema migrations (Flyway runs on app startup).
4. Deploy application container/JAR and restore configuration.
5. Point application to restored DB; validate health and key flows.

## Backup Monitoring
- Emit metrics on backup success/failure and size.
- Alert on missing backups or oversized/undersized dumps (anomaly detection).

