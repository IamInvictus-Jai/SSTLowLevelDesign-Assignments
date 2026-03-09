/**
 * Base contract for all exporters.
 * 
 * Preconditions:
 * - ExportRequest must not be null
 * - title and body fields may be null (treated as empty)
 * 
 * Postconditions:
 * - Returns ExportResult with format-specific bytes
 * - Never throws exceptions for valid input
 * - Preserves all data (no lossy conversions)
 * 
 * Subclasses MUST honor this contract.
 */
public abstract class Exporter {
    // implied "contract" but not enforced (smell)
    public abstract ExportResult export(ExportRequest req);
}
