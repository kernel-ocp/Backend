import logging
import os
from pathlib import Path

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager


class DriverFactory:
    """설정 가능한 옵션으로 Chrome WebDriver를 생성한다."""

    def __init__(
        self,
        headless: bool = False,
        driver_path: str | Path | None = None,
        cache_dir: str | Path | None = None,
    ):
        self.headless = headless
        self.driver_path = Path(driver_path) if driver_path else None
        self.cache_dir = Path(cache_dir) if cache_dir else Path.home() / ".cache" / "chromedriver"

    def create(self):
        """옵션을 구성하고 WebDriver 인스턴스를 반환한다."""
        options = webdriver.ChromeOptions()
        options.add_argument("--start-maximized")
        options.add_argument("--no-sandbox")
        options.add_argument("--disable-dev-shm-usage")
        options.add_argument("--disable-gpu")
        chromium_candidates = [
            Path("/usr/bin/chromium"),
            Path("/usr/bin/chromium-browser"),
            Path("/usr/bin/google-chrome"),
        ]
        for candidate in chromium_candidates:
            if candidate.is_file():
                options.binary_location = str(candidate)
                break
        if self.headless:
            options.add_argument("--headless=new")

        logging.info("Chrome WebDriver 시작중...")

        driver_binary = self._resolve_driver_binary()
        service = Service(str(driver_binary))

        driver = webdriver.Chrome(service=service, options=options)
        return driver

    def _resolve_driver_binary(self) -> Path:
        """시스템 설치 드라이버가 있으면 우선 사용하고, 없으면 webdriver_manager로 설치."""
        if self.driver_path and self.driver_path.is_file():
            return self.driver_path

        system_candidates = [
            Path("/usr/bin/chromedriver"),
            Path("/usr/lib/chromium-browser/chromedriver"),
            Path("/usr/lib/chromium/chromedriver"),
        ]
        for candidate in system_candidates:
            if candidate.is_file():
                return candidate

        self.cache_dir.mkdir(parents=True, exist_ok=True)
        os.environ.setdefault("WDM_LOCAL", str(self.cache_dir))
        manager = ChromeDriverManager()
        return Path(manager.install())
