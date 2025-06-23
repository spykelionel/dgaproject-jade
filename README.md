# Transformer Fault Diagnosis System – JADE Multi-Agent

## How It Works

This system standardizes fault codes from 6 diagnostic methods (KGM, IEC, RRM, DRM, DTM, MI)
into unified labels (C1–C26) using a distributed agent-based architecture (JADE).

## Input Files

Place inside `data/` folder:

- fault_codes.csv
- mappings.csv

## To Run

1. Install Java JDK
2. Place `jade.jar` in `lib/`
3. Run `compile_run.bat`

## Output

Console will display: SampleID: 1 | KGM=C3 | IEC=C15 | RRM=C8 | DRM=C20 | DTM=C24 | MI=F6

Unmapped faults will show as `UNMAPPED_Fx`.

## Agents

- DataLoaderAgent: Reads `fault_codes.csv`
- DGAMapperAgent (6x): One per method (e.g., mapper-kgm)
- ResultAggregatorAgent: Prints final fault labels

## Notes

- You may replace `fault_codes.csv` with your own
- Ensure mappings in `mappings.csv` follow format: METHOD_FCODE=C_LABEL
