Deprecated Functions - Thread Safety Issues
Problem
Several functions were deprecated due to race conditions caused by:

Spring singleton beans with field injection
Multiple threads accessing shared instance fields simultaneously
Race conditions where processes overwrite each other's data

Impact

Data Inconsistencies: DRC updates with mismatched active_user/response_due_date values
Process Variables: Inconsistent data persisted back to workflows
Detection: Hidden in staging (low load), visible in production (high concurrency)


We can't delete them because of backward compatibility, but we should avoid using them in new code
We will delete them in a future release, when we can ensure no active workflows are using them