import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useRiskStore } from '../stores/risk';

export function useRisk() {
  const riskStore = useRiskStore();
  const { header, metrics, timelineAssets, insights, chatMessages, isLoading, error } =
    storeToRefs(riskStore);

  onMounted(() => {
    void riskStore.fetchRiskData();
  });

  const handleExportPdf = () => {
    console.log('Exporting PDF risk report...');
  };

  const handleExportCsv = () => {
    console.log('Exporting CSV risk report...');
  };

  const handlePrint = () => {
    window.print();
  };

  const handleActionClick = (action: string) => {
    console.log(`Triggered risk action: ${action}`);
  };

  // Добавленная функция отправки сообщения в чат-ассистент
  const handleSendMessage = (text: string) => {
    if (!text.trim()) return;

    // Добавляем сообщение пользователя в стор/массив
    chatMessages.value.push({
      id: Date.now().toString(),
      sender: 'user',
      text,
    });

    // Имитируем ответ AI-ассистента
    setTimeout(() => {
      chatMessages.value.push({
        id: (Date.now() + 1).toString(),
        sender: 'ai',
        text: `I've processed your query about "${text}". Based on current risk metrics, portfolio concentration remains the key factor to monitor.`,
      });
    }, 800);
  };

  // Обработка кликов по быстрым подсказкам в чате
  const handleSuggestClick = (suggestion: string) => {
    handleSendMessage(suggestion);
  };

  return {
    header,
    metrics,
    timelineAssets,
    insights,
    chatMessages,
    isLoading,
    error,
    handleExportPdf,
    handleExportCsv,
    handlePrint,
    handleActionClick,
    handleSendMessage,
    handleSuggestClick,
  };
}
