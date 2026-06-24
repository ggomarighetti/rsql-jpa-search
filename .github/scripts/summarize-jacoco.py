#!/usr/bin/env python3
"""Write a compact JaCoCo coverage summary for GitHub Actions."""

from __future__ import annotations

import os
from pathlib import Path
import sys
import xml.etree.ElementTree as ET


REPORTS = [
    ("api", Path("api/target/site/jacoco/jacoco.xml")),
    ("rsql-spi", Path("rsql-spi/target/site/jacoco/jacoco.xml")),
    ("core", Path("core/target/site/jacoco/jacoco.xml")),
    ("jpa-validation", Path("jpa-validation/target/site/jacoco/jacoco.xml")),
    ("perplexhub", Path("perplexhub/target/site/jacoco/jacoco.xml")),
    ("spring-boot-starter", Path("spring-boot-starter/target/site/jacoco/jacoco.xml")),
    ("aggregate", Path("integration/target/site/jacoco-aggregate/jacoco.xml")),
]


def coverage(report: Path, counter_type: str) -> str:
    root = ET.parse(report).getroot()
    for counter in root.findall("counter"):
        if counter.attrib["type"] == counter_type:
            missed = int(counter.attrib["missed"])
            covered = int(counter.attrib["covered"])
            total = missed + covered
            if total == 0:
                return "100.00%"
            return f"{covered / total * 100:.2f}%"
    return "n/a"


def build_summary() -> str:
    lines = [
        "## Coverage",
        "",
        "| Module | Lines | Branches | Report |",
        "| --- | ---: | ---: | --- |",
    ]
    for module, report in REPORTS:
        if report.exists():
            line = coverage(report, "LINE")
            branch = coverage(report, "BRANCH")
            lines.append(f"| `{module}` | {line} | {branch} | `{report.as_posix()}` |")
        else:
            lines.append(f"| `{module}` | missing | missing | `{report.as_posix()}` |")
    lines.append("")
    return "\n".join(lines)


def main() -> int:
    summary = build_summary()
    summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
    if summary_path:
        with open(summary_path, "a", encoding="utf-8") as handle:
            handle.write(summary)
    else:
        sys.stdout.write(summary)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
