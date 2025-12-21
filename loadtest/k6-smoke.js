import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 10,
  duration: "30s",
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<500"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://app:8080";

export default function () {
  const endpoints = [
    "/api/hdds",
    "/api/laptops",
    "/api/monitors",
    "/api/pcs",
    "/api/analytics/counts",
    "/api/analytics/usage",
  ];

  for (const path of endpoints) {
    const res = http.get(`${BASE_URL}${path}`);
    check(res, {
      "status is 200": (r) => r.status === 200,
    });
  }

  sleep(1);
}
