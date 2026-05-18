import axios from "axios";

// Remplacez par l'IP de votre machine ou l'URL de l'API Gateway
export const API_BASE_URL = "http://192.168.1.100:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: { "Content-Type": "application/json" },
});

export default api;
